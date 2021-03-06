package biz.ucc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import biz.entreprise.EntrepriseDto;
import biz.factory.BizFactory;
import exceptions.BizException;
import exceptions.FatalException;
import util.AppContext;
import util.AppContext.DependanceInjection;

public class EntrepriseUccImplTest {

  @DependanceInjection
  private EntrepriseUcc entrepriseUcc;
  @DependanceInjection
  private BizFactory factory;
  @DependanceInjection
  private EntrepriseDto entreprise;

  @Before
  public void setUp() throws Exception {
    AppContext appContext = new AppContext();
    appContext.loadProps("test.properties");
    appContext.recurDepInj(this);
    entreprise = factory.getEntrepriseVide();
    entreprise.setDenomination("BNP Paribas Fortis");
    entreprise.setAdresse("Rue au flan mielleux");
    entreprise.setCodePostal("1500");
    entreprise.setNumero("15");
    entreprise.setVille("Jerusalem");
  }

  @Test
  public void testVisualiserEntreprises() {
    assertNotNull(entrepriseUcc.visualiserEntreprises());
  }

  @Test
  public void testVisualiserEntreprisesAsProf1() {
    String anneeAcademique = "2017-2018";
    assertNotNull(entrepriseUcc.visualiserEntreprisesAsProf(anneeAcademique));
  }

  @Test(expected = BizException.class)
  public void testVisualiserEntreprisesAsProf2() {
    String anneeAcademique = "0000-0000";
    entrepriseUcc.visualiserEntreprisesAsProf(anneeAcademique);
  }

  @Test(expected = BizException.class)
  public void testVisualiserEntreprisesAsProf3() {
    entrepriseUcc.visualiserEntreprisesAsProf(null);
  }

  @Test(expected = FatalException.class)
  public void testVisualiserEntreprisesAsProf4() {
    entrepriseUcc.visualiserEntreprisesAsProf("2000-2000");
  }

  @Test
  public void testGetAnneesAcademiques1() {
    assertNotNull(entrepriseUcc.getAnneesAcademiques());
  }

  @Test
  public void testGetAnneesAcademiques2() {
    assertTrue(!entrepriseUcc.getAnneesAcademiques().isEmpty());
  }

  @Test
  public void testGetEntreprise1() {
    assertNotNull(entrepriseUcc.getEntreprise(1));
  }

  @Test
  public void testGetEntreprise2() {
    assertNull(entrepriseUcc.getEntreprise(9999999));
  }

  @Test(expected = FatalException.class)
  public void testGetEntreprise3() {
    entrepriseUcc.getEntreprise(-1);
  }

  @Test(expected = BizException.class)
  public void testInsertEntreprise1() {
    entreprise.setAdresse(null);
    entrepriseUcc.insertEntreprise(entreprise);
  }

  @Test(expected = BizException.class)
  public void testInsertEntreprise2() {
    entreprise.setVille(null);
    entrepriseUcc.insertEntreprise(entreprise);
  }

  @Test(expected = BizException.class)
  public void testInsertEntreprise3() {
    entreprise.setCodePostal(null);
    entrepriseUcc.insertEntreprise(entreprise);
  }

  @Test(expected = BizException.class)
  public void testInsertEntreprise4() {
    entreprise.setDenomination(null);
    entrepriseUcc.insertEntreprise(entreprise);
  }

  @Test(expected = BizException.class)
  public void testInsertEntreprise5() {
    entreprise.setDenomination("Bosh");
    entrepriseUcc.insertEntreprise(entreprise);
  }

  @Test
  public void testInsertEntreprise6() {
    entreprise.setIdEntreprise(0);
    assertNull(entrepriseUcc.insertEntreprise(entreprise));
  }

  @Test
  public void testInsertEntreprise7() {
    entreprise.setIdEntreprise(1);
    entreprise.setTel("");
    entreprise.setEmail("");
    assertEquals(entreprise, entrepriseUcc.insertEntreprise(entreprise));
  }

  @Test(expected = BizException.class)
  public void testInsertEntreprise8() {
    entreprise.setIdEntreprise(1);
    entreprise.setTel("-1");
    entrepriseUcc.insertEntreprise(entreprise);
  }

  @Test(expected = BizException.class)
  public void testInsertEntreprise9() {
    entreprise.setIdEntreprise(1);
    entreprise.setEmail("-1");
    entrepriseUcc.insertEntreprise(entreprise);
  }

  @Test
  public void testInsertEntreprise10() {
    entreprise.setIdEntreprise(1);
    entreprise.setTel("+324974707865");
    assertEquals(entreprise, entrepriseUcc.insertEntreprise(entreprise));
  }

  @Test
  public void testInsertEntreprise11() {
    entreprise.setIdEntreprise(1);
    entreprise.setEmail("pierre@g.be");
    assertEquals(entreprise, entrepriseUcc.insertEntreprise(entreprise));
  }

  // test mauvais format email
  @Test(expected = BizException.class)
  public void testInsertEntreprise12() {
    entreprise.setIdEntreprise(1);
    entreprise.setEmail("@@@");
    entrepriseUcc.insertEntreprise(entreprise);
  }

  @Test
  public void testBlacklistEntreprise1() {
    EntrepriseDto entreprise = entrepriseUcc.blacklistEntreprise(1, 1);
    assertNotNull(entreprise);
  }

  @Test(expected = BizException.class)
  public void testBlacklistEntreprise2() {
    entrepriseUcc.blacklistEntreprise(9999999, 1);
  }

  @Test(expected = FatalException.class)
  public void testBlacklistEntreprise3() {
    entrepriseUcc.blacklistEntreprise(-1, 1);
  }

  // test id inexistant entreprise 1
  @Test(expected = BizException.class)
  public void fusionnerEntreprise1AvecEntreprise2test1() {
    entrepriseUcc.fusionnerEntreprise1AvecEntreprise2(0, 1, 9999999, 1);
  }

  // test id inexistant entreprise 2
  @Test(expected = BizException.class)
  public void fusionnerEntreprise1AvecEntreprise2test2() {
    entrepriseUcc.fusionnerEntreprise1AvecEntreprise2(9999999, 1, 0, 1);
  }

  // test entreprise 1 déjà supprimée
  @Test(expected = BizException.class)
  public void fusionnerEntreprise1AvecEntreprise2test3() {
    entrepriseUcc.fusionnerEntreprise1AvecEntreprise2(777, 1, 0, 1);
  }

  // test entreprise 2 déjà supprimée
  @Test(expected = BizException.class)
  public void fusionnerEntreprise1AvecEntreprise2test4() {
    entrepriseUcc.fusionnerEntreprise1AvecEntreprise2(0, 1, 777, 1);
  }

  // test tout fonctionne
  @Test
  public void fusionnerEntreprise1AvecEntreprise2test5() {
    assertNotNull(entrepriseUcc.fusionnerEntreprise1AvecEntreprise2(0, 1, 666, 1));
  }



}
