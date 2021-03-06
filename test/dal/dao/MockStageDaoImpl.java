package dal.dao;

import java.util.ArrayList;
import java.util.List;
import biz.factory.BizFactory;
import biz.stage.StageDto;
import exceptions.FatalException;
import util.AppContext.DependanceInjection;

public class MockStageDaoImpl implements StageDao {

  @DependanceInjection
  private BizFactory factory;

  @Override
  public StageDto getStage(int idUtilisateur) {

    if (idUtilisateur == -1) {
      throw new FatalException();
    } else if (idUtilisateur == 999 || idUtilisateur == 2 || idUtilisateur == 4
        || idUtilisateur == 5 || idUtilisateur == 6 || idUtilisateur == 7) {
      return null;
    }
    return factory.getStageVide();

  }

  @Override
  public StageDto insertStage(StageDto stage) {

    return stage;
  }

  @Override
  public List<StageDto> listerStagesPourEntreprise(int idEntreprise) {
    ArrayList<StageDto> list = new ArrayList<>();
    list.add(factory.getStageVide());
    return list;

  }

  @Override
  public StageDto updateStage(StageDto stageDto) {
    // TODO Auto-generated method stub
    return null;
  }
}
