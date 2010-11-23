package dela.ui.common

import com.vaadin.Application
import com.vaadin.terminal.FileResource
import com.vaadin.ui.Button
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.TextField

/**
 * @author vedi
 * date 03.08.2010
 * time 8:31:04
 */
class Searcher implements ClickListener {

    def entityTable
    Application application

    private TextField searchText

    def addTo(toolBar) {

        searchText = new TextField()
        toolBar.addComponent(searchText)

        def searchButton = new Button()
        searchButton.description = entityTable.i18n('button.find.label', 'button.find.label')
        searchButton.setIcon(new FileResource(new File('web-app/images/skin/search.png'), application))
        searchButton.addListener(this as ClickListener)
        toolBar.addComponent(searchButton)
    }

    def void findEntities(String searchText) {
        def table = entityTable.table

        def currentItemId = table.nextItemId(table.value?:table.firstItemId())
        def currentItem = currentItemId ? table.getItem(currentItemId) : null
        while (currentItem != null && !itemContains(currentItem, searchText)) {
            currentItemId = table.nextItemId(currentItemId)
            currentItem = currentItemId ? table.getItem(currentItemId) : null
        }

        if (currentItem != null) {
            table.select currentItemId
            table.setCurrentPageFirstItemId currentItemId
        } else {
            entityTable.application.mainWindow.showNotification(
                    entityTable.i18n('search.nothingFound.message', 'search.nothingFound.message'))
        }
    }

    boolean itemContains(Object item, String searchStr) {
        if (item != null) {
            def properties = item.getItemPropertyIds()
            return properties.find {
                def itemPropertyValue = String.valueOf(item.getItemProperty(it))
                return itemPropertyValue && itemPropertyValue.toUpperCase().contains(searchStr.toUpperCase())
            }
        } else {
            return false
        }
    }

    void buttonClick(ClickEvent event) {
        this.findEntities(searchText.value as String)
    }
}
