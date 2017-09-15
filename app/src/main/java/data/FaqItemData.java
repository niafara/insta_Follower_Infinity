package data;

/**
 * Created by Ali on 07/15/2016.
 */
public class FaqItemData
{

    private String title;
    private String content;

    public FaqItemData(String title,String content)
    {
        this.title=title;
        this.content=content;
    }

    public String getTitle()
    {
        return title;
    }

    public String getContent()
    {
        return content;
    }

}
