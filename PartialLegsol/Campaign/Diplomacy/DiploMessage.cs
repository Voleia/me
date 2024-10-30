public class DiploMessage : I_Message
{
    string title;
    string body;
    string cons;
    string button;
    
    public DiploMessage(string title_, string body_, string cons_, string button_) {
        title = title_;
        body = body_;
        cons = cons_;
        button = button_;
    }

    public string getTitle() {
        return title;
    }
    public string getBody() { return body; }
    public string getCons() { return cons; }
    public string getButton() { return button; }

    public MessageType getType() {
        return MessageType.MESSAGE;
    }
}
