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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the user a text box to add and remove tags that enables drilling
 * down into the set of items.
 *
 * @author Elizeu Santos-Neto (elizeus at ece dot ubc dot ca)
 */
public class SearchBarWidget extends FlexTable
    implements ClickHandler, KeyUpHandler {
  /**
   * A text box where the user can add a new tag
   */
  TextBox newTagBox;

  /**
   * The list of tags added by the user
   */
  List<String> tags;
  List<Button> tagButtons;

  /**
   * A button that allows the user add a new tag to the search criteria
   */
  Image addTag;

  /**
   * Cell coordinate of the next tag
   */
  int cellIdx;

  public SearchBarWidget() {
    tags = new ArrayList<String>();
    tagButtons = new ArrayList<Button>();
    newTagBox = new TextBox();
    newTagBox.addKeyUpHandler(this);
    TagValueImageBundle icons = GWT.create(TagValueImageBundle.class);
    addTag = new Image(icons.selectIcon());
    addTag.addClickHandler(this);
    setWidget(0, 0, newTagBox);
    setWidget(0, 1, addTag);
    cellIdx = 0;
  }

  @Override
  public void onClick(ClickEvent event) {
    String tag = newTagBox.getText();
    Object source = event.getSource();
    if ((source == addTag) && !hasTag(tag) && !tag.isEmpty()) {
      addNewTag();
    } else if (tagButtons.contains(source)) {
      Cell sourceCell = getCellForEvent(event);
      int row = sourceCell.getRowIndex();
      int col = sourceCell.getCellIndex();
      Widget button = getWidget(row, col);
      int buttonIdx = tagButtons.indexOf(button);
      int tagIdx = tags.indexOf(tagButtons.get(buttonIdx).getText());
      tagButtons.remove(buttonIdx);
      tags.remove(tagIdx);
      remove(button);
      removeCell(row, col);
      cellIdx--;
    }
  }

  public List<String> getTags() {
    return tags;
  }

  public boolean hasTag(String tag) {
    return tags.contains(tag);
  }

  @Override
  public void onKeyUp(KeyUpEvent event) {
    Object source = event.getSource();
    String tag = newTagBox.getText();
    if ((source == newTagBox)
        && (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) && !hasTag(tag)
        && !tag.isEmpty()) {
      addNewTag();
    }
  }

  private void addNewTag() {
    tags.add(newTagBox.getText());
    Button newTagButton = new Button(newTagBox.getText());
    newTagButton.addClickHandler(this);
    newTagButton.addKeyUpHandler(this);
    tagButtons.add(newTagButton);
    insertCell(0, cellIdx);
    setWidget(0, cellIdx, newTagButton);
    cellIdx++;
    newTagBox.setText("");
  }

  public void addNewTag(String tag) {
    if ((tag != null) && !(tag.isEmpty() || tags.contains(tag))) {
      newTagBox.setText(tag);
      addNewTag();
    }
  }

  public HandlerRegistration addClickHandler(ClickHandler handler) {
    for (Button button : tagButtons) {
      button.addClickHandler(handler);
    }
    addTag.addClickHandler(handler);
    return addDomHandler(handler, ClickEvent.getType());
  }

  public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
    newTagBox.addKeyUpHandler(handler);
    return addDomHandler(handler, KeyUpEvent.getType());
  }
}
