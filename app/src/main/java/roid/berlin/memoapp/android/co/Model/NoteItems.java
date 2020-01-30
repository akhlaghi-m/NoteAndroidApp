package roid.berlin.memoapp.android.co.Model;

public class NoteItems {

    private String uTitle;
    private String uDescription;
    private int uImage;
    private int uImageVoice;

    public String getuTitle() {
        return uTitle;
    }

    public void setuTitle(String uTitle) {
        this.uTitle = uTitle;
    }

    public String getuDescription() {
        return uDescription;
    }

    public void setuDescription(String uDescription) {
        this.uDescription = uDescription;
    }

    public int getuImage() {
        return uImage;
    }

    public void setuImage(int uImage) {
        this.uImage = uImage;
    }

    public int getuImageVoice() {
        return uImageVoice;
    }

    public void setuImageVoice(int uImageVoice) {
        this.uImageVoice = uImageVoice;
    }
}
