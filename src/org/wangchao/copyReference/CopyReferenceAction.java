/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wangchao.copyReference;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.*;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.datatransfer.ExClipboard;

/**
 *
 * @author wangchao
 */
public class CopyReferenceAction extends CallableSystemAction {


    public FileObject getFileObject(Document doc) {
        Object o = doc.getProperty(Document.StreamDescriptionProperty);
        if (o instanceof DataObject) {
            return ((DataObject) o).getPrimaryFile();
        } else if (o instanceof FileObject) {
            return (FileObject) o;
        } else if (o != null) {
        }
        return null;
    }

    @Override
    public void performAction() {

        JTextComponent editor = EditorRegistry.lastFocusedComponent();

        Document doc = editor.getDocument();
        FileObject f = getFileObject(doc);
        String path = "";
        int linenumber = 0;
        try {
            if (f != null) {
                path = f.getPath();
            }
            String t = editor.getText(0, editor.getCaretPosition());
            String thinT = t.replaceAll("\n", "");
            linenumber = t.length() - thinT.length() + 1;
        } catch (BadLocationException ex) {
            linenumber = 0;
            path = "";
        }

        Clipboard clipboard = Lookup.getDefault().lookup(ExClipboard.class);
        if (clipboard == null) {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        }
        if (clipboard != null) {
            clipboard.setContents(new StringSelection(path + ":" + linenumber), null);
        }

    }

    @Override
    public String getName() {
        return "Copy reference";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("copy file reference");
    }

}
