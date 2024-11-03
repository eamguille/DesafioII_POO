
package vistas.swing;

import vistas.eventos.EventMenuSelected;
import modelo.MenuModelo;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

public class ListMenu<E extends Object> extends JList<E> {
    
    private final DefaultListModel model;
    private int selectedIndex = -1;
    private int overIndex = -1;
    private EventMenuSelected event;
    
    public void addEventMenuSelected(EventMenuSelected event) {
        this.event = event;
    }
    
    public ListMenu() {
        model = new DefaultListModel();
        setModel(model);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if(SwingUtilities.isLeftMouseButton(me)) {
                    int index = locationToIndex(me.getPoint());
                    Object o = model.getElementAt(index);
                    if(o instanceof MenuModelo) {
                        MenuModelo menu = (MenuModelo) o;
                        if(menu.getType() == MenuModelo.MenuType.MENU) {
                            selectedIndex = index;
                            if(event != null) {
                                event.selected(index);
                            }
                        }
                    } else {
                        selectedIndex = index;
                    }
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent me) {
                overIndex = -1;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
           @Override
           public void mouseMoved(MouseEvent me) {
               int index = locationToIndex(me.getPoint());
               if(index != overIndex) {
                   Object o = model.getElementAt(index);
                   if(o instanceof MenuModelo) {
                       MenuModelo menu = (MenuModelo) o;
                       if(menu.getType() == MenuModelo.MenuType.MENU) {
                           overIndex = index;
                       } else {
                           overIndex = -1;
                       }
                       repaint();
                   }
               }
           }
        });
    }
    
    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int index, boolean selected, boolean focus) {
                MenuModelo data;
                if (o instanceof MenuModelo) {
                    data = (MenuModelo) o;
                } else {
                    data = new MenuModelo("", o + "", MenuModelo.MenuType.EMPTY);
                }
                MenuItem item = new MenuItem(data);
                item.setSelected(selectedIndex == index);
                item.setOver(overIndex == index);
                return item;
            }
        };
    }
    
    public void addItem(MenuModelo data) {
        model.addElement(data);
    }
    
}
