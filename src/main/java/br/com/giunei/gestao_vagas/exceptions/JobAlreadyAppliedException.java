package br.com.giunei.gestao_vagas.exceptions;

public class JobAlreadyAppliedException extends RuntimeException {

    public JobAlreadyAppliedException() {
        super("Essa vaga já foi aplicada por você");
    }
}
