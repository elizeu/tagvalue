/*
 * Copyright (c) 2012
 *
 * NetSysLab - ECE - UBC
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package ca.ubc.ece.netsyslab.tagvalue.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Represents the first page which the user will interact with. The goal is to
 * collect its username so we can connect its navigation trace to the bookmarks
 * she submits separately.
 *
 * @author Elizeu Santos-Neto (elizeus at ece dot ubc dot ca)
 */
public class UsernameWidget extends FlexTable
  implements ClickHandler, KeyUpHandler {
  /**
   * A widget to get the username
   */
  private TextBox usernameBox;

  /**
   * A button used by the
   */
  private Button startButton;

  /** Store the username after the click */
  private String username;

  /**
   * Instantiates a new authorization widget.
   *
   * @param service a proxy to the backend service that guides the user.
   */
  public UsernameWidget() {
    username = "";
    usernameBox = new TextBox();
    usernameBox.setText("username");
    usernameBox.setTitle("Type your del.icio.us username in the box");
    usernameBox.setFocus(true);
    usernameBox.selectAll();
    usernameBox.addClickHandler(this);
    usernameBox.addKeyUpHandler(this);

    startButton = new Button();
    startButton.setText("Start");
    startButton.setTitle("Click here to give consent and start the experiment");
    startButton.addStyleName("sendButton");
    startButton.addClickHandler(this);

    setWidget(0, 0, usernameBox);
    setWidget(0, 1, startButton);
  }

  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
    return addDomHandler(handler, KeyUpEvent.getType());
  }

  public String getUsername() {
    return username;
  }

  @Override
  public void onClick(ClickEvent event) {
    if (event.getSource() == startButton) {
      username = usernameBox.getText().trim();
    }
  }

  @Override
  public void onKeyUp(KeyUpEvent event) {
    if ((event.getSource() == usernameBox)
        && (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)) {
      username = usernameBox.getText().trim();
    }
  }
}
