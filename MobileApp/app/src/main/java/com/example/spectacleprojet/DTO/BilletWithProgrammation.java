package com.example.spectacleprojet.DTO;



public class BilletWithProgrammation {
    private BilletDTO billet;
    private ProgrammationDTO programmation;

    public BilletWithProgrammation(BilletDTO billet, ProgrammationDTO programmation) {
        this.billet = billet;
        this.programmation = programmation;
    }

    public BilletDTO getBillet() {
        return billet;
    }

    public ProgrammationDTO getProgrammation() {
        return programmation;
    }
}

