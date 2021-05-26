public class ButtonValues {
    private boolean ifClicked;
    private boolean ifPicked;

    public ButtonValues()
    {
        ifClicked = false;
        ifPicked = false;
    }

    public boolean ifClickedFunction(){return ifClicked;}
    public boolean ifPickedFunction(){return ifPicked;}

    public void pickValue()
    {
        ifPicked = true;
    }
    public void clickValue() { ifClicked = true; }
}
