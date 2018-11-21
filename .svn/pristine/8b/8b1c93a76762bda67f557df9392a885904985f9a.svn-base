package biz.entreprise;

public class EntrepriseImpl implements EntrepriseBiz {

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
    result = prime * result + ((boite == null) ? 0 : boite.hashCode());
    result = prime * result + ((codePostal == null) ? 0 : codePostal.hashCode());
    result = prime * result + ((denomination == null) ? 0 : denomination.hashCode());
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + (estBlackListe ? 1231 : 1237);
    result = prime * result + (estSupprime ? 1231 : 1237);
    result = prime * result + idEntreprise;
    result = prime * result + numVersion;
    result = prime * result + ((numero == null) ? 0 : numero.hashCode());
    result = prime * result + ((tel == null) ? 0 : tel.hashCode());
    result = prime * result + ((ville == null) ? 0 : ville.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    EntrepriseImpl other = (EntrepriseImpl) obj;
    if (adresse == null) {
      if (other.adresse != null) {
        return false;
      }
    } else if (!adresse.equals(other.adresse)) {
      return false;
    }
    if (boite == null) {
      if (other.boite != null) {
        return false;
      }
    } else if (!boite.equals(other.boite)) {
      return false;
    }
    if (codePostal == null) {
      if (other.codePostal != null) {
        return false;
      }
    } else if (!codePostal.equals(other.codePostal)) {
      return false;
    }
    if (denomination == null) {
      if (other.denomination != null) {
        return false;
      }
    } else if (!denomination.equals(other.denomination)) {
      return false;
    }
    if (email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!email.equals(other.email)) {
      return false;
    }
    if (estBlackListe != other.estBlackListe) {
      return false;
    }
    if (estSupprime != other.estSupprime) {
      return false;
    }
    if (idEntreprise != other.idEntreprise) {
      return false;
    }
    if (numVersion != other.numVersion) {
      return false;
    }
    if (numero == null) {
      if (other.numero != null) {
        return false;
      }
    } else if (!numero.equals(other.numero)) {
      return false;
    }
    if (tel == null) {
      if (other.tel != null) {
        return false;
      }
    } else if (!tel.equals(other.tel)) {
      return false;
    }
    if (ville == null) {
      if (other.ville != null) {
        return false;
      }
    } else if (!ville.equals(other.ville)) {
      return false;
    }
    return true;
  }

  // attributs
  private int idEntreprise;
  private String denomination;
  private String adresse;
  private String numero;
  private String boite;
  private String codePostal;
  private String ville;
  private String email;
  private String tel;
  private boolean estBlackListe;
  private boolean estSupprime;

  private int numVersion;

  @Override
  public void fillEntreprise(int idEntreprise, String denomination, String adresse, String numero,
      String boite, String codePostal, String ville, String email, String tel,
      boolean estBlackListe, int numVersion, boolean estSupprime) {
    this.idEntreprise = idEntreprise;
    this.denomination = denomination;
    this.adresse = adresse;
    this.numero = numero;
    this.boite = boite;
    this.codePostal = codePostal;
    this.ville = ville;
    this.email = email;
    this.tel = tel;
    this.estBlackListe = estBlackListe;
    this.numVersion = numVersion;
    this.estSupprime = estSupprime;
  }

  @Override
  public String toString() {
    return "EntrepriseImpl [idEntreprise=" + idEntreprise + ", denomination=" + denomination
        + ", adresse=" + adresse + ", numero=" + numero + ", boite=" + boite + ", codePostal="
        + codePostal + ", ville=" + ville + ", email=" + email + ", tel=" + tel + ", estBlackListe="
        + estBlackListe + ", numVersion=" + numVersion + "]";
  }

  public int getIdEntreprise() {
    return idEntreprise;
  }

  public void setIdEntreprise(int idEntreprise) {
    this.idEntreprise = idEntreprise;
  }

  public String getDenomination() {
    return denomination;
  }

  public void setDenomination(String denomination) {
    this.denomination = denomination;
  }

  public String getAdresse() {
    return adresse;
  }

  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getBoite() {
    return boite;
  }

  public void setBoite(String boite) {
    this.boite = boite;
  }

  public String getCodePostal() {
    return codePostal;
  }

  public void setCodePostal(String codePostal) {
    this.codePostal = codePostal;
  }

  public String getVille() {
    return ville;
  }

  public void setVille(String ville) {
    this.ville = ville;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public boolean getEstBlackListe() {
    return estBlackListe;
  }

  public void setEstBlackListe(boolean estBlackListe) {
    this.estBlackListe = estBlackListe;
  }

  public int getNumVersion() {
    return numVersion;
  }

  public void setNumVersion(int numVersion) {
    this.numVersion = numVersion;
  }

  public boolean getEstSupprime() {
    return estSupprime;
  }

  public void setEstSupprime(boolean estSupprime) {
    this.estSupprime = estSupprime;
  }
}
