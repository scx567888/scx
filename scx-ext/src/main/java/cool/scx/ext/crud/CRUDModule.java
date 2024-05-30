package cool.scx.ext.crud;

import cool.scx.core.Scx;
import cool.scx.core.ScxModule;

import static java.lang.System.Logger;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.getLogger;

/**
 * 为 BaseModel 的实现类 提供一套简单的 "单表" 的增删改查 api
 *
 * @author scx567888
 * @version 1.3.0
 */
public class CRUDModule extends ScxModule {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = getLogger(CRUDModule.class.getName());

    /**
     * <p>Constructor for CmsModule.</p>
     */
    public CRUDModule() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Scx scx) {
        //这里添加额外的参数处理器 保证 CRUDListParam 类型的参数永不为空
        scx.scxWeb().addParameterHandler(0, cool.scx.ext.crud.CRUDListParamMethodParameterHandler.DEFAULT_INSTANCE);
        scx.scxWeb().addParameterHandler(0, cool.scx.ext.crud.CRUDUpdateParamMethodParameterHandler.DEFAULT_INSTANCE);
        logger.log(DEBUG, "已添加用于处理类型为 CRUDListParam   的 MethodParameterHandler  -->  {0}", CRUDListParamMethodParameterHandler.class.getName());
        logger.log(DEBUG, "已添加用于处理类型为 CRUDUpdateParam 的 MethodParameterHandler  -->  {0}", CRUDUpdateParamMethodParameterHandler.class.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "SCX_EXT-" + super.name();
    }

}
