package util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import exceptions.BizException;
import exceptions.FatalException;

public class Util {


  private Util() {

  }

  /**
   * Transforme une exception en String.
   * 
   * @param ex l'exception à transformer en String
   * @return une chaîne de caractère semblable à printstackTrace()
   */
  public static String stackTraceToString(Exception ex) {

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    ex.printStackTrace(printWriter);
    String stackTraceAsString = stringWriter.toString();
    return stackTraceAsString;
  }

  /**
   * Extrait la date d'une chaîne de caractères JSON.
   * 
   * @param json Chaîne de caractères JSON
   * @param nomChampDate Nom de la clé JSON possédant la date
   * @return La date sous format LocalDate
   * @throws BizException une exception
   */
  public static LocalDate jsonToLocalDate(String json, String nomChampDate) {
    LocalDate dateParse = null;
    Genson genson = new GensonBuilder().useDateFormat(new SimpleDateFormat("dd-MM-yyyy")).create();
    Map<String, Object> mapData = genson.deserialize(json, Map.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String date = mapData.get(nomChampDate).toString();
    checkFormatString(date, 10, "^([0-2][0-9]|3[01])/(0[0-9]|1[0-2])/[0-9]{4}$",
        "Le format de la date est incorrect");
    try {
      dateParse = LocalDate.parse(date, formatter);
    } catch (DateTimeParseException ex) {
      throw new BizException("Le format de la date est incorrect");
    }
    return dateParse;
  }

  /**
   * Vérifie et renvoie l'année académique en cours en fonction de la date passée en paramètre.
   * 
   * @param date la date à vérifier
   * @return l'année académique
   */
  public static String localDateToYear(LocalDate date) {
    checkObject(date);
    int year = date.getYear();
    if (date.getMonth().compareTo(Month.JANUARY) < 0) {
      return year + "-" + year + 1;
    }
    return year - 1 + "-" + year;
  }



  /**
   * Vérifie le format d'une string selon plusieurs paramètres.
   * 
   * @param string la phrase à vérifier
   * @param maxCaracteres le nombre de caractères max autorisé
   * @param regex l'expression régulière
   * @param messageErreur une exception
   */
  public static void checkFormatString(String string, int maxCaracteres, String regex,
      String messageErreur) {
    if (string == null || string.isEmpty() || string.length() > maxCaracteres
        || !string.matches(regex)) {
      throw new BizException(messageErreur);
    }
  }

  /**
   * Vérifie le format d'une string selon plusieurs paramètres.
   * 
   * @param string la phrase à vérifier
   * @param maxCaracteres le nombre de caractères max autorisé
   * @param messageErreur une exception
   */
  public static void checkFormatString(String string, int maxCaracteres, String messageErreur) {
    if (string == null || string.isEmpty() || string.length() > maxCaracteres) {
      throw new BizException(messageErreur);
    }
  }

  /**
   * Vérifie si la date passé en paramètre est null ou postérieure à la date courante.
   * 
   * @param date la date à vérifier
   * @param messageErreur une exception
   */
  public static void checkFormatDate(LocalDate date, String messageErreur) {
    if (date == null || date.isAfter(LocalDate.now())) {
      throw new BizException(messageErreur);
    }
  }

  /**
   * Vérifie si l'objet n'est pas null.
   * 
   * @param ob Objet à vérifier
   */
  public static void checkObject(Object ob) {
    if (ob == null) {
      throw new FatalException("Erreur de programmation");
    }
  }

  /**
   * Vérifie si la chaîne est non vide et non null.
   * 
   * @param st Chaîne à vérifier
   * @throws BizException une exception
   */
  public static void checkString(String st) {
    checkObject(st);
    if (st.matches("\\s*")) {
      throw new FatalException("Erreur de programmation");
    }
  }

  /**
   * Vérifie si la chaîne est convertible en nombre.
   * 
   * @param st Chaîne à vérifier
   * @throws BizException une exception
   */
  public static void checkNumerique(String st) {
    checkString(st);
    try {
      Long.parseLong(st);
    } catch (NumberFormatException ex) {
      throw new BizException("");
    }
  }

  /**
   * Vérifie si le nombre est positif ou égal à zéro.
   * 
   * @param nombre Nombre à vérifier
   * @throws BizException une exception
   */
  public static void checkPositiveOrZero(double nombre) {
    if (nombre < 0) {
      throw new BizException("");
    }
  }

  /**
   * Vérifie si le nombre est strictement positif.
   * 
   * @param nombre Nombre à vérifier
   * @throws BizException une exception
   */
  public static void checkPositive(double nombre) {
    if (nombre <= 0) {
      throw new BizException("");
    }
  }

  /**
   * Vérifie si l'entier est strictement positif.
   * 
   * @param nombre Nombre à vérifier
   * @throws BizException une exception
   */
  public static void checkInteger(int nombre) {
    if (nombre <= 0) {
      throw new BizException("");
    }
  }

  /**
   * Crypte la chaîne passée en paramètre avec le sel défini.
   * 
   * @param mdp Chaîne à crypter
   * @return La chaîne cryptée
   */
  public static String hashpw(String mdp) {
    return BCrypt.hashpw(mdp, BCrypt.gensalt());
  }

  /**
   * Vérifie si les deux chaînes sont les mêmes.
   * 
   * @param mdp Chaîne non cryptée à comparer
   * @param mdpHashed Chaîne cryptée à comparer
   * @return True si les chaînes sont identiques, false sinon
   */
  public static boolean checkpw(String mdp, String mdpHashed) {
    return BCrypt.checkpw(mdp, mdpHashed);
  }



}
