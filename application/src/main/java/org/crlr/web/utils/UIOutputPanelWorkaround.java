package org.crlr.web.utils;

import java.util.Properties;

import org.richfaces.component.UIOutputPanel;

public class UIOutputPanelWorkaround extends UIOutputPanel
    {
      public boolean isKeepTransient() {
        // or just return false;
        Boolean value = (Boolean) getStateHelper().eval(Properties.keepTransient, false);
        return value;
      }
    
}
