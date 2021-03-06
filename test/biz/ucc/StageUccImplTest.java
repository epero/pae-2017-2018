package biz.ucc;

import static org.junit.Assert.assertNotNull;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import biz.factory.BizFactory;
import biz.stage.StageDto;
import exceptions.BizException;
import exceptions.FatalException;
import exceptions.OptimisticLockException;
import util.AppContext;
import util.AppContext.DependanceInjection;

public class StageUccImplTest {

  @DependanceInjection
  private StageUcc stageUcc;
  @DependanceInjection
  private BizFactory factory;

  private StageDto stageAVerif;


  @Before
  public void setUp() throws Exception {
    AppContext appContext = new AppContext();
    appContext.loadProps("test.properties");
    appContext.recurDepInj(this);
    stageAVerif = factory.getStageVide();
  }

  @Test
  public void testGetStage01() {
    assertNotNull(stageUcc.visualiserStage(1));
  }

  @Test
  public void testGetStage02() {
    assertNotNull(stageUcc.visualiserStage(998));
  }

  @Test(expected = BizException.class)
  public void testGetStage03() {
    stageUcc.visualiserStage(99999);
  }


  @Test(expected = FatalException.class)
  public void testGetStage04() {
    stageUcc.visualiserStage(-1);
  }

  @Test(expected = BizException.class)
  public void testGetStage05() {
    stageUcc.visualiserStage(999);
  }


  // Test de base passant tous les tests
  @Test
  public void confirmDataStage0() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  // Test de base passant tous les tests avec une boite à null (car pas obligatoire)
  @Test
  public void confirmDataStage01() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite(null);
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage02() {
    stageAVerif.setAdresse("Avenue de l'avenue des sapins pliés"); // incorrect
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage03() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("123456"); // incorrect
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage04() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("12345"); // incorrect
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage05() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille(null); // incorrect
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage06() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(null); // incorrect
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage07() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 99999, 99999); // userDb null
  }


  @Test(expected = BizException.class)
  public void confirmDataStage08() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(9999999); // entrepriseDb null
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage09() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(Integer.MAX_VALUE); // personneContactDb null
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage10() {
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, Integer.MAX_VALUE, Integer.MAX_VALUE, 2, 2); // contactDb
                                                                                        // null
  }

  @Test(expected = OptimisticLockException.class)
  public void confirmDataStage11() {
    stageAVerif.setAdresse("Avenue des marteaux");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageUcc.confirmDataStage(stageAVerif, 1, 5, 2, 2); // contactNumVersion != contactDb
  }

  @Test(expected = OptimisticLockException.class)
  public void confirmDataStage12() {
    stageAVerif.setAdresse("Avenue des marteaux");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 5); // userNumVersion != userDb
  }

  @Test(expected = BizException.class)
  public void confirmDataStage13() {
    // stageDao.getStage(idUser) != null
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 3, 3);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage14() {
    // !pcDao.personneDeContactAppartientEntreprise(stage.getResponsable(),
    // stage.getEntreprise())
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(3);
    stageAVerif.setEntreprise(3);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 2, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage15() {
    // (contactDao.getContacts(idUser, Etat.STAGE_EN_ORDRE.getNumEtat()).size() != 0)
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 4, 4);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage16() {
    // List<ContactDto> contactsAcceptes = contactDao.getContacts(idUser,
    // Etat.ACCEPTE.getNumEtat());
    // contactsAcceptes.size() != 1
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 5, 5);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage17() {
    // ContactDto contactAccepteDb = contactsAcceptes.get(0);
    // contactAccepteDb.getIdContact() != idContact
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 6, 6);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage18() {
    // ContactDto contactAccepteDb = contactsAcceptes.get(0);
    // contactAccepteDb.getEntreprise() != (stage.getEntreprise()
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 7, 7);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage19() {
    // userDb.getEstAdmin()
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 777, 2);
  }

  @Test(expected = BizException.class)
  public void confirmDataStage20() {
    // !userDb.getAnneeAcademique().equals(Util.localDateToYear(LocalDate.now()))
    stageAVerif.setAdresse("Avenue");
    stageAVerif.setBoite("12345");
    stageAVerif.setCodePostal("1234");
    stageAVerif.setVille("Bruxelles");
    stageAVerif.setDateSignature(LocalDate.of(2018, 3, 3));
    stageAVerif.setResponsable(2);
    stageAVerif.setEntreprise(2);
    stageUcc.confirmDataStage(stageAVerif, 1, 1, 778, 2);
  }



}
