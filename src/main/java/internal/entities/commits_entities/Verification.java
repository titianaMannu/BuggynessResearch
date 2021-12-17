
package internal.entities.commits_entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Verification implements Serializable {

	@SerializedName("verified")
	@Expose
	private boolean verified;
	@SerializedName("reason")
	@Expose
	private String reason;
	@SerializedName("signature")
	@Expose
	private String signature;
	@SerializedName("payload")
	@Expose
	private String payload;
	private static final long serialVersionUID = 1014003411075653269L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Verification() {
	}

	/**
	 * 
	 * @param reason
	 * @param signature
	 * @param payload
	 * @param verified
	 */
	public Verification(boolean verified, String reason, String signature, String payload) {
		super();
		this.verified = verified;
		this.reason = reason;
		this.signature = signature;
		this.payload = payload;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public Verification withVerified(boolean verified) {
		this.verified = verified;
		return this;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Verification withReason(String reason) {
		this.reason = reason;
		return this;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Verification withSignature(String signature) {
		this.signature = signature;
		return this;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Verification withPayload(String payload) {
		this.payload = payload;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("verified", verified).append("reason", reason)
				.append("signature", signature).append("payload", payload).toString();
	}

}
