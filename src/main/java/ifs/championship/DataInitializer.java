package ifs.championship;

import ifs.championship.model.*;
import ifs.championship.model.enums.CourseLevel;
import ifs.championship.model.enums.EventStatus;
import ifs.championship.repository.*;
import ifs.championship.service.ClassificationService;
import ifs.championship.service.GroupService;
import ifs.championship.service.KnockoutPhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    // Injeções de todas as dependências necessárias
    @Autowired private CourseRepository courseRepository;
    @Autowired private SportRepository sportRepository;
    @Autowired private CoordinatorRepository coordinatorRepository;
    @Autowired private AthleteRepository athleteRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private CaptainRepository captainRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private InscriptionRepository inscriptionRepository;
    @Autowired private MatchRepository matchRepository;
    @Autowired private GroupService groupService; // Serviço para criar grupos
    @Autowired private ClassificationService classificationService; // Serviço para criar a fase de grupos
    @Autowired private KnockoutPhaseService knockoutPhaseService; // Serviço para criar o mata-mata

    @Override
    public void run(String... args) throws Exception {
        System.out.println("### Iniciando a população completa do banco de dados... ###");

        // --- PASSO 1: CRIAR E PERSISTIR DADOS BÁSICOS ---
        Sport futsal = sportRepository.save(createSport("Futsal", 5, 10));

        List<Course> courses = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            courses.add(createCourse("Curso de Tecnologia " + i, CourseLevel.SUPERIOR));
        }
        courseRepository.saveAll(courses);

        List<Athlete> athletes = athleteRepository.saveAll(createAthletes(30));
        Coordinator coordinator = coordinatorRepository.save(createCoordinator("Prof. Ada Lovelace", "admin123", courses.get(0), "Ada@hotmail.com"));
        Event eventFutsal = eventRepository.save(createEvent("Copa de Futsal 2025", CourseLevel.SUPERIOR));

        // --- PASSO 2: CRIAR UMA EQUIPE PARA CADA CURSO E INSCREVÊ-LAS ---
        for (int i = 0; i < 6; i++) {
            Course cursoAtual = courses.get(i);

            int startIndex = i * 5;
            int endIndex = startIndex + 5;
            List<Athlete> jogadores = athletes.subList(startIndex, endIndex);
            Athlete tecnico = jogadores.get(0); // O primeiro jogador do bloco é o capitão/técnico

            captainRepository.save(createCaptain(tecnico, cursoAtual, futsal));
            Team equipe = teamRepository.save(createTeam("Time do " + cursoAtual.getName(), cursoAtual, futsal, tecnico, jogadores));
            inscriptionRepository.save(subscribeEquipeNoEvento(equipe, eventFutsal));
        }

        // --- PASSO 3: GERAR OS GRUPOS ---
        System.out.println("Gerando a FASE DE GRUPOS para o evento: " + eventFutsal.getName());
        List<Group> groupStage = groupService.generateGroupsForEvent(eventFutsal.getId());

        for (Group group : groupStage) {
            System.out.println("Grupo " + group.getName() + " criado com " + group.getInscriptions().size() + " equipes.");
        }

        // --- PASSO 4: GERAR AS PARTIDAS DOS GRUPOS ---
        System.out.println("Gerando partidas para os grupos...");
        for (Group group : groupStage) {
            groupService.generateMatchesForGroups(group, group.getInscriptions());
        }

        // --- PASSO 5:  GERAR A CLASSIFICAÇÃO ---
        for (Group group : groupStage) {
            List<Match> matches = matchRepository.findByGroupId(group.getId());
            for (Match match : matches) {
                registrarPlacar(match, 2, 1);
            }
        }

        // --- PASSO 5: GERAR A FASE ELIMINATÓRIA ---
        System.out.println("Gerando a FASE ELIMINATÓRIA para o evento: " + eventFutsal.getName());
        List<Match> eliminatoryMatches = knockoutPhaseService.createKnockoutPhase(eventFutsal.getId());
        System.out.println("Detalhes das partidas eliminatórias:");
        for (Match match : eliminatoryMatches) {
            String teamA = match.getTeamA().getName();
            String teamB = match.getTeamB().getName();
            String status = match.getStatus();
            String phase = match.getPhase();
            System.out.printf("Confronto: %s x %s | Status: %s | Fase: %s \n", teamA, teamB, status, phase);
        }

        // --- PASSO 6: SIMULAR RESULTADOS DAS PARTIDAS ELIMINATÓRIAS ---
        Random rand = new Random();
        for (Match match : eliminatoryMatches) {
            int placarA = rand.nextInt(6); // placar entre 0 e 5
            int placarB = rand.nextInt(6);
            registrarPlacar(match, placarA, placarB);
        }

        for (Match match : eliminatoryMatches) {
            String teamA = match.getTeamA().getName();
            String teamB = match.getTeamB().getName();
            int placarA = match.getTeamAScore();
            int placarB = match.getTeamBScore();
            String status = match.getStatus();
            System.out.printf("Resultado FASE ELIMINATÓRIA: %s %d x %d %s | STATUS: %s\n", teamA, placarA, placarB, teamB, status);
        }
    }

    // --- MÉTODOS AUXILIARES ---
    private Course createCourse(String nome, CourseLevel nivel) {
        Course course = new Course(); course.setName(nome); course.setLevel(nivel); return course;
    }
    private Sport createSport(String nome, int min, int max) {
        Sport sport = new Sport(); sport.setName(nome); sport.setMinAthletes(min); sport.setMaxAthletes(max); return sport;
    }
    private Coordinator createCoordinator(String nome, String senha, Course course, String email) {
        Coordinator c = new Coordinator(); c.setName(nome); c.setPass(senha); c.setCourse(course); c.setEmail(email); return c;
    }
    private List<Athlete> createAthletes(int quantidade) {
        List<Athlete> athletes = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            Athlete a = new Athlete(); a.setFullName("Athlete " + i); a.setNickName("Player" + i); a.setPass("senha"); athletes.add(a);
        }
        return athletes;
    }
    private Event createEvent(String nome, CourseLevel nivel) {
        Event e = new Event(); e.setName(nome); e.setCourseLevel(nivel); e.setStatus(EventStatus.FINALIZADO); return e;
    }
    private Captain createCaptain(Athlete tecnico, Course course, Sport sport) {
        Captain td = new Captain(); td.setAthlete(tecnico); td.setCourse(course); td.setSport(sport); return td;
    }
    private Team createTeam(String nome, Course course, Sport sport, Athlete tecnico, List<Athlete> jogadores) {
        Team eq = new Team(); eq.setName(nome); eq.setCourse(course); eq.setSport(sport); eq.setTechnical(tecnico); eq.setAthletes(jogadores); return eq;
    }
    private Inscription subscribeEquipeNoEvento(Team equipe, Event evento) {
        Inscription i = new Inscription(); i.setTeam(equipe); i.setEvent(evento); return i;
    }
    private void registrarPlacar(Match jogo, int placarA, int placarB) {
        jogo.setTeamAScore(placarA); jogo.setTeamBScore(placarB); jogo.setStatus("FINALIZADO"); matchRepository.save(jogo);
    }
}