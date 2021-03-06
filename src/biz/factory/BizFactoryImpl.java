package biz.factory;

import biz.contact.ContactDto;
import biz.contact.ContactImpl;
import biz.entreprise.EntrepriseDto;
import biz.entreprise.EntrepriseImpl;
import biz.pdc.PersonneContactDto;
import biz.pdc.PersonneContactImpl;
import biz.stage.StageDto;
import biz.stage.StageImpl;
import biz.user.UserDto;
import biz.user.UserImpl;

class BizFactoryImpl implements BizFactory {

  @Override
  public UserDto getUserVide() {
    return new UserImpl();
  }

  @Override
  public PersonneContactDto getPersonneContactVide() {
    return new PersonneContactImpl();
  }

  @Override
  public EntrepriseDto getEntrepriseVide() {
    return new EntrepriseImpl();
  }

  @Override
  public ContactDto getContactVide() {
    return new ContactImpl();
  }

  @Override
  public StageDto getStageVide() {
    return new StageImpl();
  }
}
