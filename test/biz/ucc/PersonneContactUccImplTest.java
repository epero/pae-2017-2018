package biz.ucc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import biz.factory.BizFactory;
import biz.pdc.PersonneContactDto;
import exceptions.BizException;
import exceptions.FatalException;
import util.AppContext;
import util.AppContext.DependanceInjection;

public class PersonneContactUccImplTest {

  @DependanceInjection
  private PersonneContactUcc personneContactUcc;
  @DependanceInjection
  private BizFactory factory;

  private PersonneContactDto personneContact;

  @Before
  public void setUp() throws Exception {
    AppContext appContext = new AppContext();
    appContext.loadProps("test.properties");
    appContext.recurDepInj(this);
    personneContact = factory.getPersonneContactVide();
  }

  @Test
  public void testVisualiserPersonnesContact1() {
    List<PersonneContactDto> personnesContactDto = personneContactUcc.visualiserPersonnesContact(1);
    assertNotNull(personnesContactDto);
  }

  @Test
  public void testVisualiserPersonnesContact2() {
    List<PersonneContactDto> personnesContactDto = personneContactUcc.visualiserPersonnesContact(1);
    assertFalse(personnesContactDto.isEmpty());
  }

  @Test
  public void testVisualiserPersonnesContact3() {
    List<PersonneContactDto> personnesContactDto =
        personneContactUcc.visualiserPersonnesContact(Integer.MAX_VALUE);
    assertTrue(personnesContactDto.isEmpty());
  }

  @Test(expected = FatalException.class)
  public void testVisualiserPersonnesContact4() {
    personneContactUcc.visualiserPersonnesContact(-2);
  }

  @Test(expected = BizException.class)
  public void testVisualiserPersonnesContact5() {
    personneContactUcc.visualiserPersonnesContact(9999999);
  }

  @Test(expected = FatalException.class)
  public void testCreerPersonneContact1() {
    personneContact.setIdPersonneContact(1);
    personneContact.setNom("bubu");
    personneContact.setPrenom("baba");
    personneContact.setTel("+32472284635");
    personneContact.setEmail("baba@hotmail.com");
    personneContact.setEntreprise(-1);
    personneContactUcc.creerPersonneContact(personneContact);
  }

  @Test(expected = BizException.class)
  public void testCreerPersonneContact1Bis() {
    personneContact.setIdPersonneContact(1);
    personneContact.setNom("bubu");
    personneContact.setPrenom("baba");
    personneContact.setTel(null); // Incorrect
    personneContact.setEmail("baba@hotmail.com");
    personneContact.setEntreprise(1);
    personneContactUcc.creerPersonneContact(personneContact);
  }

  @Test(expected = BizException.class)
  public void testCreerPersonneContact1BisBis() {
    personneContact.setIdPersonneContact(1);
    personneContact.setNom("bubu");
    personneContact.setPrenom("baba");
    personneContact.setTel("+32472284635");
    personneContact.setEmail("baba"); // Incorrect
    personneContact.setEntreprise(-1);
    personneContactUcc.creerPersonneContact(personneContact);
  }

  @Test
  public void testCreerPersonneContact2() {
    personneContact.setIdPersonneContact(1);
    personneContact.setNom("bubu");
    personneContact.setPrenom("baba");
    personneContact.setTel("+32472284635");
    personneContact.setEmail("baba@hotmail.com");
    personneContact.setEntreprise(1);
    PersonneContactDto personneContactDto =
        personneContactUcc.creerPersonneContact(personneContact);
    assertNotNull(personneContactDto);
    assertEquals(personneContact, personneContactDto);
  }

  @Test(expected = BizException.class)
  public void testCreerPersonneContact3() {
    personneContact.setIdPersonneContact(1);
    personneContact.setNom("azertyuiopazertyuiopazertyuiopazertyuiopazertyuiopd"); // 51 caractères
    personneContact.setPrenom("baba");
    personneContact.setTel("696969696969");
    personneContact.setEmail("baba@hotmail.com");
    personneContact.setEntreprise(1);

    personneContactUcc.creerPersonneContact(personneContact);
  }

  @Test(expected = BizException.class)
  public void testCreerPersonneContact4() {
    personneContact.setIdPersonneContact(1);
    personneContact.setNom("bubu");
    personneContact.setPrenom("azertyuiopazertyuiopazertyuiopa"); // 31 caractères
    personneContact.setTel("696969696969");
    personneContact.setEmail("baba@hotmail.com");
    personneContact.setEntreprise(1);

    personneContactUcc.creerPersonneContact(personneContact);
  }

  @Test(expected = BizException.class)
  public void testCreerPersonneContact5() {
    personneContact.setIdPersonneContact(1);
    personneContact.setNom("bubu");
    personneContact.setPrenom("baba");
    personneContact.setTel("azertyuiopazerty"); // 16 caractères
    personneContact.setEmail("baba@hotmail.com");
    personneContact.setEntreprise(1);

    personneContactUcc.creerPersonneContact(personneContact);
  }

  @Test(expected = BizException.class)
  public void testCreerPersonneContact6() {
    personneContact.setIdPersonneContact(1);
    personneContact.setNom("bubu");
    personneContact.setPrenom("baba");
    personneContact.setTel("696969696969");
    personneContact.setEmail("azertyuiopazertyuiopazertyuiopazertyuiopazertyuiopd");
    personneContact.setEntreprise(1);

    personneContactUcc.creerPersonneContact(personneContact);
  }

  @Test(expected = BizException.class)
  public void testCreerPersonneContact7() {
    personneContact.setIdPersonneContact(1);
    personneContact.setNom("bubu");
    personneContact.setPrenom("baba");
    personneContact.setTel("696969696969");
    personneContact.setEmail("baba@hotmail.com");
    personneContact.setEntreprise(9999999);

    personneContactUcc.creerPersonneContact(personneContact);
  }

  @Test
  public void testVisualiserResponsablesStage1() {
    assertNotNull(personneContactUcc.visualiserResponsablesStage());
  }

  @Test
  public void testVisualiserResponsablesStage2() {
    assertTrue(personneContactUcc.visualiserResponsablesStage().isEmpty());
  }

}
