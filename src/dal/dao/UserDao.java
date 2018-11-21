package dal.dao;

import java.util.HashMap;
import java.util.List;
import biz.user.UserDto;

public interface UserDao {

  /**
   * Récupère les informations d'un utilisateur en base de données.
   * 
   * @param pseudo Une chaîne de caractère représentant le pseudo de l'utilisateur
   * @return un UserDto si l'utilisateur a pu être récupéré en base de données, null sinon
   */
  public UserDto getUser(String pseudo);

  /**
   * Récupère les informations d'un utilisateur en base de données.
   * 
   * @param idUtilisateur l'identifiant de l'utilisateur
   * @return un UserDto si l'utilisateur a pu être récupérer en base de données, null sinon
   */
  public UserDto getUser(int idUtilisateur);

  /**
   * Récupère et renvoie une liste d'utilisateurs pour une année académique donnée.
   * 
   * @param anneeAcademique l'année académique
   * @return une liste de UserDto pour l'année académique donnée
   */
  public List<UserDto> getAllUsers(String anneeAcademique);


  /**
   * insert un nouvel utilisateur dans la base de donnees.
   * 
   * @param user l utilisateur a inserer
   * @return un UserDto si l utilisateur a ete insere, null sinon
   */
  public UserDto insertUser(UserDto user);

  /**
   * verifie si la table utilisateurs en db contient le pseudo passe en parametre.
   *
   * @param pseudo le pseudo a verifier
   * @return true si le pseudo existe dans la table utilisateurs, false sinon
   */
  public boolean pseudoUserExiste(String pseudo);

  /**
   * verifie si la table utilisateurs en db contient l email passe en parametre.
   *
   * @param email l'email à vérifier
   * @return true si l email existe dans la table utilisateurs, false sinon
   */
  public boolean emailUserExiste(String email);

  /**
   * Met à jour les informations de l'utilisateur sauf le mot de passe.
   * 
   * @param user l'utilisateur à mettre à jour
   * @return un UserDto si la mise à jour est faite, null sinon
   */
  public UserDto updateUser(UserDto user);

  /**
   * Récupère les statistiques de tous les étudiant d'une année académique.
   * 
   * @param anneeAcademique Une chaîne de caractère représentant l'année académique
   * @return Un dictionnaire qui a comme clé l'état le plus avancé et comme valeur le nombre
   *         d'étudiant étant dans cet état
   */
  public HashMap<String, Integer> getStudentsStats(String anneeAcademique);

  /**
   * Récupère les statistiques d'un étudiant.
   * 
   * @param idUser Le numéro d'utilisateur de l'étudiant
   * @return Un dictionnaire qui a comme clé l'état le plus avancé et comme valeur le nombre
   *         d'étudiant étant dans cet état
   */
  public HashMap<String, Integer> getStudentStats(int idUser);



}
