package pl.greenislanddev.prawojazdy.business;

public class CategoryItem {

	private String categoryName;
	private String chapter;

	public CategoryItem(String categoryName, String chapter) {
		super();
		this.categoryName = categoryName;
		this.chapter = chapter;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

}
