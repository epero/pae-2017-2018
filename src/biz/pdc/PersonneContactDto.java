package biz.pdc;

import biz.entreprise.EntrepriseDto;

public interface PersonneContactDto {

  /**
   * Rempli la personne de contact avec les différents paramètres.
   * 
   * @param idPersonneContact le numéro de la personne de contact
   * @param nom une chaîne de caractère représentant le nom de la personne de contact
   * @param prenom une chaîne de caractère représentant le prénom de la personne de contact
   * @param tel une chaîne de caractère représentant le téléphone de la personne de contact
   * @param email une chaîne de caractère représentant l'email de la personne de contact
   * @param entreprise le numéro de l'entreprise appartenant à la personne de contact
   * @param entrepriseDto un objet de type EntrepriseDto représentant l'entreprise
   * @param numVersion le numéro de version de la personne de contact
   */
  public void fillPersonneContact(int idPersonneContact, String nom, String prenom, String tel,
      String email, int entreprise, EntrepriseDto entrepriseDto, int numVersion);

  public int getIdPersonneContact();

  public String getNom();

  public String getPrenom();

  public String getTel();

  public String getEmail();

  public int getEntreprise();

  public EntrepriseDto getEntrepriseDto();

  public int getNumVersion();

  public void setIdPersonneContact(int idPersonneContact);

  public void setNom(String nom);

  public void setPrenom(String prenom);

  public void setTel(String tel);

  public void setEmail(String email);

  public void setEntreprise(int entreprise);

  public void setEntrepriseDto(EntrepriseDto entrepriseDto);

  public void setNumVersion(int numVersion);

}
