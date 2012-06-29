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

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * An image bundle used to build buttons.
 *
 * @author Elizeu Santos-Neto (elizeus at ece dot ubc dot ca)
 */
public interface TagValueImageBundle extends ClientBundle {
  @Source("icon/Stop.png")
  ImageResource removeIcon();

  @Source("icon/Select.png")
  ImageResource selectIcon();
}
