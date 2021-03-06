package biz.ucc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import biz.factory.BizFactory;
import biz.user.UserDto;
import dal.services.DalServices;
import exceptions.BizException;
import exceptions.FatalException;
import util.AppContext;
import util.AppContext.DependanceInjection;

public class UserUccImplTest {
  @DependanceInjection
  private UserUcc userUcc;
  private UserDto userAVerif;
  @DependanceInjection
  private BizFactory factory;
  @DependanceInjection
  private DalServices dalServices;

  @Before
  public void setUp() throws Exception {
    AppContext appContext = new AppContext();
    appContext.loadProps("test.properties");
    appContext.recurDepInj(this);
    userAVerif = factory.getUserVide();
  }

  @Test
  public void testSeConnecter1() {
    UserDto user = userUcc.seConnecter("blehmann", "azerty");
    assertNotNull(user);
  }

  @Test(expected = BizException.class)
  public void testSeConnecter2() {
    userUcc.seConnecter("", "azerty");
  }

  @Test(expected = BizException.class)
  public void testSeConnecter3() {
    userUcc.seConnecter("blehmann", "");
  }

  @Test
  public void testSeConnecter4() {
    UserDto user = userUcc.seConnecter("pierre", "azerty");
    assertNull(user);
  }

  @Test(expected = FatalException.class)
  public void testSeConnecter5() {
    userUcc.seConnecter("@@@@@", "azerty");
  }

  @Test
  public void testSeConnecter6() {
    userUcc.seConnecter("blehmann", "qwerty");
  }

  @Test
  public void testSinscire1() {
    userAVerif.setPseudo("blehmann");
    userAVerif.setMdp("azerty");
    userAVerif.setDateNaissance(LocalDate.now().minusYears(30));
    userAVerif.setEmail("brigitte.lehmann@vinci.be");
    userAVerif.setNom("brigitte");
    userAVerif.setPrenom("lehmann");
    userAVerif.setTel("+32495707865");

    UserDto user = userUcc.sinscrire(userAVerif);
    assertNotNull(user);
    assertEquals(user.getPseudo(), userAVerif.getPseudo());

  }

  @Test(expected = BizException.class)
  public void testSinscire2() {
    userAVerif.setPseudo("pierre");
    userAVerif.setMdp("azerty");
    userAVerif.setDateNaissance(LocalDate.now().minusYears(30));
    userAVerif.setEmail("brigitte.lehmann@vinci.be");
    userAVerif.setNom("brigitte");
    userAVerif.setPrenom("lehmann");
    userAVerif.setTel("+32495707865");

    userUcc.sinscrire(userAVerif);

  }

  @Test(expected = BizException.class)
  public void testSinscire3() {
    userAVerif.setPseudo("blehmann");
    userAVerif.setMdp("azerty");
    userAVerif.setDateNaissance(LocalDate.now().minusYears(30));
    userAVerif.setEmail("pierre@vinci.be");
    userAVerif.setNom("brigitte");
    userAVerif.setPrenom("lehmann");
    userAVerif.setTel("+32495707865");

    userUcc.sinscrire(userAVerif);

  }

  @Test(expected = BizException.class)
  public void testSinscire4() {
    userAVerif.setPseudo("");
    userAVerif.setMdp("azerty");
    userAVerif.setDateNaissance(LocalDate.now().minusYears(30));
    userAVerif.setEmail("pierre@vinci.be");
    userAVerif.setNom("brigitte");
    userAVerif.setPrenom("lehmann");
    userAVerif.setTel("+32495707865");

    userUcc.sinscrire(userAVerif);

  }

  @Test(expected = BizException.class)
  public void testSinscire5() {
    userAVerif.setPseudo("blehmann");
    userAVerif.setMdp("azerty");
    userAVerif.setDateNaissance(LocalDate.now().minusYears(30));
    userAVerif.setEmail("blehmann@vinci.be");
    userAVerif.setNom("brigitte");
    userAVerif.setPrenom("lehmann");
    userAVerif.setTel("+32495707865dazehdfeohfze");

    userUcc.sinscrire(userAVerif);

  }

  @Test(expected = BizException.class)
  public void testSinscire6() {
    userAVerif.setPseudo("blehmann");
    userAVerif.setMdp("azerty");
    userAVerif.setDateNaissance(null);
    userAVerif.setEmail("blehmann@vinci.be");
    userAVerif.setNom("brigitte");
    userAVerif.setPrenom("lehmann");
    userAVerif.setTel("+32495707865");

    userUcc.sinscrire(userAVerif);

  }

  @Test(expected = BizException.class)
  public void testSinscire7() {
    userAVerif.setPseudo("blehmann");
    userAVerif.setMdp("azerty");
    userAVerif.setDateNaissance(LocalDate.now().plusYears(30));
    userAVerif.setEmail("blehmann@vinci.be");
    userAVerif.setNom("brigitte");
    userAVerif.setPrenom("lehmann");
    userAVerif.setTel("+32495707865");

    userUcc.sinscrire(userAVerif);

  }

  @Test
  public void testTrouverUtilisateurById1() {
    UserDto user = userUcc.trouverUtilisateurById(1);
    assertNotNull(user);
  }

  @Test
  public void testTrouverUtilisateurById2() {
    UserDto user = userUcc.trouverUtilisateurById(99999);
    assertNull(user);
  }

  @Test(expected = FatalException.class)
  public void testTrouverUtilisateurById3() {
    UserDto user = userUcc.trouverUtilisateurById(-1);
    assertNull(user);
  }

  @Test
  public void testUpdateInfoUtilisateur1() {
    UserDto user = userUcc.updateInfoUtilisateur(100, 1, "test", "test", LocalDate.of(1990, 1, 1),
        "test.test@student.vinci.be", "+32497878787");
    assertNotNull(user);
  }

  @Test(expected = BizException.class)
  public void testUpdateInfoUtilisateur2() {
    userUcc.updateInfoUtilisateur(99999, 1, "test", "test", LocalDate.of(1990, 1, 1),
        "test.test@student.vinci.be", "+32497878787");
  }

  @Test
  public void testUpdateMdpUtilisateur1() {
    UserDto user = userUcc.updateMdpUtilisateur(100, 1, "mdpuser", "mdpuser", "mdpuser");
    assertNotNull(user);
  }

  @Test(expected = BizException.class)
  public void testUpdateMdpUtilisateur2() {
    userUcc.updateMdpUtilisateur(99999, 1, "mdpuser", "mdpuser", "mdp");
  }

  @Test(expected = BizException.class)
  public void testUpdateMdpUtilisateur3() {
    userUcc.updateMdpUtilisateur(99999, 1, "mdpuser", "mdpuser", "mdpuser");
  }

  @Test(expected = BizException.class)
  public void testUpdateMdpUtilisateur4() {
    userUcc.updateMdpUtilisateur(99999, 1, "mdp", "mdpuser", "mdpuser");
  }

  @Test(expected = BizException.class)
  public void testUpdateMdpUtilisateur5() {
    userUcc.updateMdpUtilisateur(100, 1, "qgrgg", "mdpuser", "mdpuser");
  }

  @Test
  public void getStudentsStats1() {
    assertNotNull(userUcc.getStudentsStats());
  }

  @Test
  public void getStudentStats1() {
    assertNotNull(userUcc.getStudentStats(100));
  }

  @Test(expected = BizException.class)
  public void getStudentStats2() {
    assertNotNull(userUcc.getStudentStats(99999));
  }

  @Test(expected = FatalException.class)
  public void getStudentStats3() {
    assertNotNull(userUcc.getStudentStats(-1));
  }

  @Test(expected = FatalException.class)
  public void getStudentStats4() {
    assertNotNull(userUcc.getStudentStats(-2));
  }

  @Test
  public void testVisualiserUsersAnneeCourante1() {
    assertNotNull(userUcc.visualiserUsersAnneeCourante());
  }

  @Test
  public void testVisualiserUsersAnneeCourante2() {
    assertTrue(userUcc.visualiserUsersAnneeCourante().isEmpty());
  }


}
