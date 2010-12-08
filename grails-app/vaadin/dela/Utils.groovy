package dela

import com.vaadin.data.util.BeanItem
import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * @author vedi
 * date 06.12.10
 * time 17:43
 */
@Category(Object)
class Utils {

    final def getDomain(item) {
        (item as BeanItem).bean
    }

    final def getEntityListCaption(dataContext) {
        messageService.getEntityListCaptionMsg(dataContext.domainClass.simpleName.toLowerCase())
    }

    final def getEntityCaption(dataContext) {
        messageService.getEntityCaptionMsg(dataContext.domainClass.simpleName.toLowerCase())
    }

    final def getFieldLabel(dataContext, column) {
        messageService.getFieldLabelMsg(dataContext.domainClass.simpleName.toLowerCase(), column)
    }

    final def getGridFields(dataContext) {
        dataContext.domainClass.withTransaction {
            dataContext.domainClass.newInstance().properties.collect {it.key}   //  TODO: Tests
        }
    }

    final def getFormFields(dataContext) {
        getGridFields(dataContext)
    }

    final def getFile(fileName) {
        return ApplicationHolder.application.parentContext.getResource(fileName).file
    }

}
