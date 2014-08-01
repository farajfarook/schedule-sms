package com.enbiso.proj.schedulesms.form.wizard;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farflk on 7/31/2014.
 */
public class TemplateItem {
    private String name;
    private String content;

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public TemplateItem(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public static List<TemplateItem> fetchTemplateItems(Context context){
        List<TemplateItem> templateItems = new ArrayList<TemplateItem>();
        templateItems.add(new TemplateItem("Happy birthday", "Happy Birthday!!! I hope this is the begining of your greatest, most wonderful year ever!"));
        templateItems.add(new TemplateItem("All the best", "Good luck is the willing handmaid of a upright and energetic character, and conscientious observance of duty."));
        templateItems.add(new TemplateItem("Anniversary", "That special day is here again, The day we took our vows, You’re just as special to me today, As you still get me aroused. Happy Anniversary."));
        templateItems.add(new TemplateItem("Christmas", "Merry Christmas indeed. I had thought Happy Christmas was a British thing as I heard it in the late 80s while I was living there. If it is the media, damn them and lumps of coal in their stockings."));
        templateItems.add(new TemplateItem("Children’s Day", "Let’s join hands on Universal Children’s Day… To make this world a safer place for the little ones! Happy Children’s Day..!!"));
        templateItems.add(new TemplateItem("Exam", "To accomplish great things, We must not only act, But also dream, Not only plan but also believe, Best wishes for your exam."));
        return templateItems;
    }
}
