package biz.user;

import java.io.Serializable;
import java.time.LocalDate;

public interface UserDto extends Serializable {

  public String toString();

  /**
   * Rempli l'utilisateur avec ses différentes propriétés.
   * 
   * @param idUtilisateur le numéro de l'utilisateur
   * @param pseudo le pseudo de l'utilisateur
   * @param mdp le mot de passe de l'utilisateur
   * @param nom le nom de l'utilisateur
   * @param prenom le prenom de l'utilisateur
   * @param dateNaissance la date de naissance de l'utilisateur
   * @param tel le téléphone de l'utilisateur
   * @param email l'email de l'utilisateur
   * @param dateInscription la date d'inscription de l'utilisateur
   * @param anneeAcademique l'année académique de l'utilisateur
   * @param estAdmin un booléen qui informe si l'utilisateur est admin
   * @param nbContacts un nombre représentant le nombre de contact de l'utilisateur avec des
   *        entreprises
   * @param etatPlusAvance un numero représentant le numero de l'état de son contact avec une
   *        entreprise le plus avancé
   * @param numVersion le numéro de version del'utilisateur
   */
  public void fillUser(int idUtilisateur, String pseudo, String mdp, String nom, String prenom,
      LocalDate dateNaissance, String tel, String email, LocalDate dateInscription,
      String anneeAcademique, boolean estAdmin, int nbContacts, int etatPlusAvance, int numVersion);

  // getters
  public int getIdUtilisateur();

  public String getPseudo();

  public String getMdp();

  public String getNom();

  public String getPrenom();

  public LocalDate getDateNaissance();

  public String getTel();

  public String getEmail();

  public LocalDate getDateInscription();

  public String getAnneeAcademique();

  public boolean getEstAdmin();

  public int getNbContacts();

  public int getNumVersion();


  // setters
  public void setIdUtilisateur(int idUtilisateur);

  public void setPseudo(String pseudo);

  public void setMdp(String mdp);

  public void setNom(String nom);

  public void setPrenom(String prenom);

  public void setDateNaissance(LocalDate dateNaissance);

  public void setTel(String tel);

  public void setEmail(String email);

  public void setDateInscription(LocalDate dateInscription);

  public void setAnneeAcademique(String anneeAcademique);

  public void setEstAdmin(boolean estAdmin);

  public void setNbContacts(int nbContacts);

  public void setNumVersion(int numVersion);

  public int getEtatPlusAvance();

  public void setEtatPlusAvance(int etatPlusAvance);
}
