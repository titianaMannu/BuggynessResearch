package internal.entities.jira_entities.bugs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Version implements Serializable {

	@SerializedName("self")
	@Expose
	private String vself;
	@SerializedName("id")
	@Expose
	private String vid;
	@SerializedName("description")
	@Expose
	private String vdescription;
	@SerializedName("name")
	@Expose
	private String vname;
	@SerializedName("archived")
	@Expose
	private boolean varchived;
	@SerializedName("released")
	@Expose
	private boolean vreleased;
	@SerializedName("releaseDate")
	@Expose
	private String vreleaseDate;
	private static final long serialVersionUID = -5059180345562114090L;

	public String getVself() {
		return vself;
	}

	public void setVself(String vself) {
		this.vself = vself;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getVdescription() {
		return vdescription;
	}

	public void setVdescription(String vdescription) {
		this.vdescription = vdescription;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public boolean isVarchived() {
		return varchived;
	}

	public void setVarchived(boolean varchived) {
		this.varchived = varchived;
	}

	public boolean isVreleased() {
		return vreleased;
	}

	public void setVreleased(boolean vreleased) {
		this.vreleased = vreleased;
	}

	public String getVreleaseDate() {
		return vreleaseDate;
	}

	public void setVreleaseDate(String vreleaseDate) {
		this.vreleaseDate = vreleaseDate;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("self", vself).append("id", vid).append("description", vdescription)
				.append("name", vname).append("archived", varchived).append("released", vreleased)
				.append("releaseDate", vreleaseDate).toString();
	}

}
