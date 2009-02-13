package org.jboss.tools.flow.jpdl4.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

public class DetailsView extends PageBookView implements ISelectionProvider,
        ISelectionChangedListener {

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        getSelectionProvider().addSelectionChangedListener(listener);
    }

    protected IPage createDefaultPage(PageBook book) {
        MessagePage page = new MessagePage();
        initPage(page);
        page.createControl(book);
        page.setMessage("Details are not available.");
        return page;
    }

    protected PageRec doCreatePage(IWorkbenchPart part) {
    	Object obj = part.getAdapter(IDetailsPage.class);
    	if (obj instanceof IDetailsPage) {
    		IDetailsPage page = (IDetailsPage)obj;
    		if (page instanceof IPageBookViewPage) {
    			initPage((IPageBookViewPage)page);
    		}
    		page.createControl(getPageBook());
    		return new PageRec(part, page);
    	}
    	return null;
    }

    protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
        IDetailsPage page = (IDetailsPage) rec.page;
        page.dispose();
        rec.dispose();
    }

    protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage page = getSite().getPage();
        if (page != null) {
			return page.getActiveEditor();
		}
        return null;
    }

    public ISelection getSelection() {
        return getSelectionProvider().getSelection();
    }

    protected boolean isImportant(IWorkbenchPart part) {
        return (part instanceof IEditorPart);
    }

    public void partBroughtToTop(IWorkbenchPart part) {
        partActivated(part);
    }

    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        getSelectionProvider().removeSelectionChangedListener(listener);
    }

    public void selectionChanged(SelectionChangedEvent event) {
        getSelectionProvider().selectionChanged(event);
    }

    public void setSelection(ISelection selection) {
        getSelectionProvider().setSelection(selection);
    }

    protected void showPageRec(PageRec pageRec) {
        IPageSite pageSite = getPageSite(pageRec.page);
        ISelectionProvider provider = pageSite.getSelectionProvider();
        if (provider == null && (pageRec.page instanceof IDetailsPage)) {
			// This means that the page did not set a provider during its initialization 
            // so for backward compatibility we will set the page itself as the provider.
            pageSite.setSelectionProvider((IDetailsPage) pageRec.page);
		}
        super.showPageRec(pageRec);
    }
}
