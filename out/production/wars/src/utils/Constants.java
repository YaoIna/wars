package utils;

public class Constants {
    public static String BATTLE = "Battle";
    public static String COUNTRY = "Country";
    public static String TIME = "Time";
    public static String CASUALTIES = "Casualties";

    public static String PRINT_HELP = "You can print this map by clicking on this print button";
    public static String ARROW_HELP = "You can cancel or reset any tool you have now by clicking on this pointer button";
    public static String MEASURE_HELP = "You can draw a line by click-drag-release your mouse and see its distance showing on the " +
            "status label in the bottom of the map after clicking on this button";
    public static String PICK_ICON_HELP = "You can choose a icon for a selected point layer by clicking on this pick icon button(you must select a point layer before you use this button)";
    public static String HOTLINK_HELP = "You can click on a point that has hotlink to see more about this point by this hotlink button(you must select a point layer before you use this button)";
    public static String ADD_LAYER_HELP = "You can browse your computer and choose a shape file to add a new layer by clicking this add layer button";

    public static String TABLE_OF_CONTENT = "    The toc, or table of contents, is to the left of the map. \n" +
            "    Each entry is called a 'legend' and represents a map 'layer' or \n" +
            "    'theme'.  If you click on a legend, that layer is called the \n" +
            "    active layer, or selected layer.  Its display (rendering) properties \n" +
            "    can be controlled using the Legend Editor, and the legends can be \n" +
            "    reordered using Layer Control.  Both Legend Editor and Layer Control \n" +
            "    are separate Help Topics.";
    public static String LEGEND_EDITOR = "    The Legend Editor is a menu item found under the File menu. \n" +
            "    Given that a layer is selected by clicking on its legend in the table of \n" +
            "    contents, clicking on Legend Editor will open a window giving you choices \n" +
            "    about how to display that layer.  For example you can control the color \n" +
            "    used to display the layer on the map, or whether to use multiple colors ";
    public static String LAYER_CONTROL = "    Layer Control is a Menu on the menu bar.  If you have selected a \n" +
            " layer by clicking on a legend in the toc (table of contents) to the left of \n" +
            " the map, then the promote and demote tools will become usable.  Clicking on \n" +
            " promote will raise the selected legend one position higher in the toc, and \n" +
            " clicking on demote will lower that legend one position in the toc.";
    public static String HELP_TOOL = "    This tool will allow you to learn about certain other tools. \n" +
            "    You begin with a standard left mouse button click on the Help Tool itself. \n" +
            "    RIGHT click on another tool and a window may give you information about the  \n" +
            "    intended usage of the tool.  Click on the arrow tool to stop using the \n" +
            "    help tool.";

    public static String CONTACT = "\n\n\n\n                 Any enquiries should be addressed to " +
            "\n\n\n                         wangyao4ina@gmail.com";

    public static String READ_ME = "This application is to show 20 famous battles in World Wide War II based on MOJO 2.0. Users can use hotlink tool to click on a icon of the battles to see" +
            "more information about this battle(including text, pic and web link). One can also select any areas of the map and save them as a map file. If you don't like the battle icons, you can" +
            "pick a icon using pick icon tool. Other functions are also provided. For example, add layer, measure distance, print map, legend editor and so on. Here is a picture of a pet with me.";
    public static String[] NAME_BUNDLE_KEYS_MENU = {"file", "theme", "layer_control", "help", "language"};
    public static String[] NAME_BUNDLE_KEYS_FILE = {"add", "print", "remove", "legend"};
    public static String[] NAME_BUNDLE_KEYS_THEME = {"attribute_table", "create_selection", "create_csv", "pick_icons"};
    public static String[] NAME_BUNDLE_KEYS_LAYER = {"promote", "demote"};
    public static String[] NAME_BUNDLE_KEYS_HELP = {"help_topics", "contact_us", "about_MOJO", "read_me"};
    public static String[] NAME_BUNDLE_KEYS_HELP_TOPICS = {"table_content", "legend", "layer_control", "help_tool"};
    public static String[] NAME_BUNDLE_KEYS_HELP_LANGUAGE = {"english", "chinese"};


}
