package edu.scu.crawler;

import java.util.ArrayList;
/**
 * 
 * @author ���
 *
 */

//����Ϊ����࣬��ȡ�������������
public class Result {
    private String title;//����
    private String url;//����
    private String summary;//��Ҫ
    private String basicInfo;//������Ϣ
    private String index;//Ŀ¼
    private String context;//��������
    private String reference;//�ο����ף������ӣ�
    private String tags;//��ǩ
    private String statics;//����ͳ�ƣ���ȡ��ʱ�䡢���ٴα༭���ϴθ���ʱ�䣩
    
    private ArrayList<String> urlLink ;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public ArrayList<String> getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(ArrayList<String> urlLink) {
        this.urlLink = urlLink;
    }
    public String toString()
	{
		return "Title:"+title+"\nContext:\n"+context+"\nUrlLink:"+urlLink+"\n";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getStatics() {
		return statics;
	}

	public void setStatics(String statics) {
		this.statics = statics;
	}
}
