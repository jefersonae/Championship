package ifs.championship.dto;

import lombok.Data;

@Data
public class ResultDTO {
    private int scoreTeam1;
    private int scoreTeam2;
    private boolean wo;
    // Se 'wo' for true, este campo indica qual equipe venceu.
    // Pode ser 'A' ou 'B', correspondendo à equipeA ou equipeB do jogo.
    private Character winner;
}