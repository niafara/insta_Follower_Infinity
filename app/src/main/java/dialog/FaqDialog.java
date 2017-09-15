package dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import adapter.FaqAdapter;
import data.FaqItemData;
import afm.niafara.instagram.R;

/**
 * Created by Ali on 07/15/2016.
 */
public class FaqDialog extends Dialog
{
    public FaqDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.faq_layout);

        ListView listView = (ListView)findViewById(R.id.list_view);
        findViewById(R.id.close).setOnClickListener(onClickListener);

        SetUpItems(listView);
    }

    private void SetUpItems(ListView listView)
    {
        String[] titles =  getContext().getResources().getStringArray(R.array.faq_titles);
        String[] contents =  getContext().getResources().getStringArray(R.array.faq_contents);

        FaqItemData[] faqItemDatas = new FaqItemData[titles.length];
        for(int i=0;i<faqItemDatas.length;i++)
        {
            faqItemDatas[i] = new FaqItemData(titles[i],contents[i]);
        }

        FaqAdapter faqAdapter = new FaqAdapter(getContext(),R.layout.faq_item,faqItemDatas);

        listView.setAdapter(faqAdapter);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FaqDialog.this.dismiss();
        }
    };
}
