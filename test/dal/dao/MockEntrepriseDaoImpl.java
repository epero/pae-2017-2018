package dal.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import biz.entreprise.EntrepriseDto;
import biz.factory.BizFactory;
import exceptions.FatalException;
import util.AppContext.DependanceInjection;

public class MockEntrepriseDaoImpl implements EntrepriseDao {
  @DependanceInjection
  private BizFactory factory;

  @Override
  public List<EntrepriseDto> getAllEntreprises() {
    List<EntrepriseDto> entreprises = new ArrayList<>();
    return entreprises;
  }

  @Override
  public List<HashMap<String, Object>> getAllEntreprisesWithNumberOfStudents(
      String anneeAcademique) {
    if (anneeAcademique.equals("2000-2000")) {
      throw new FatalException();
    }
    HashMap<String, Object> map = new HashMap<>();
    List<HashMap<String, Object>> entreprises = new ArrayList<>();
    entreprises.add(map);
    return entreprises;
  }

  @Override
  public List<String> getAnneesAcademiques() {
    List<String> anneesAcademiques = new ArrayList<>();
    return anneesAcademiques;
  }

  @Override
  public EntrepriseDto getEntreprise(int idEntreprise) {
    if (idEntreprise == -1) {
      throw new FatalException();
    } else if (idEntreprise == 9999999) {
      return null;
    } else if (idEntreprise == 777) {
      EntrepriseDto entreprise = factory.getEntrepriseVide();
      entreprise.setIdEntreprise(idEntreprise);
      entreprise.setEstSupprime(true);
      return entreprise;
    } else if (idEntreprise == 666) {
      EntrepriseDto entreprise = factory.getEntrepriseVide();
      entreprise.setDenomination("denomination");
      return entreprise;
    }
    EntrepriseDto entrepriseDto = factory.getEntrepriseVide();
    entrepriseDto.setIdEntreprise(idEntreprise);
    return entrepriseDto;
  }



  @Override
  public EntrepriseDto insertEntreprise(EntrepriseDto entreprise) {
    if (entreprise.getIdEntreprise() % 2 == 0) {
      return null;
    } else {
      return entreprise;
    }
  }

  @Override
  public boolean denominationEntrepriseExiste(String denomination) {
    return denomination.equals("Bosh");
  }

  @Override
  public EntrepriseDto updateEntreprise(EntrepriseDto entreprise) {
    entreprise.setNumVersion(entreprise.getNumVersion() + 1);
    return entreprise;
  }


}
