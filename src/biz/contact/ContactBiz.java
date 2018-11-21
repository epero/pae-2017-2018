package biz.contact;

public interface ContactBiz extends ContactDto {
  enum Etat {
    INITIE("initie", 0), REFUSE("refuse", 1), PRIS("pris", 2), ACCEPTE("accepte",
        3), STAGE_EN_ORDRE("stageEnOrdre", 4);

    private final String nomEtat;
    private final int numEtat;

    @Override
    public String toString() {
      return this.nomEtat;
    }

    Etat(String nomEtat, int numEtat) {
      this.nomEtat = nomEtat;
      this.numEtat = numEtat;
    }

    public String getNomEtat() {
      return nomEtat;
    }

    public int getNumEtat() {
      return numEtat;
    }


  }
}
