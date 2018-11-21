package dal.services;

public interface DalServices {

  /**
   * Set l'autoCommit à false pour la connexion active ssi le compteur de transaction est nul. A
   * chaque appel de cette méthode, le compteur de transaction est incrémenté.
   */
  public void startTransaction();

  /**
   * Décrémente le compteur de transaction ss'il est supérieur à 0. Commit la (les) transaction(s)
   * et set l'autoCommit à true ssi le compteur de transaction est nul. Appelle la méthode
   * rollback() en cas d'erreur.
   */
  public void commitTransaction();

  /**
   * Réinitalise le compteur de transaction. Rollback la transaction.
   */
  public void rollback();



}
