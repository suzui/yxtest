package jsons;

public class Result {

	public boolean status;
	public String msg;
	public String html;
	public String url;
	public Object data;

	public Result(boolean status, String msg, String url, Object data) {
		this.status = status;
		this.msg = msg;
		this.url = url;
		this.data = data;
	}

	public Result(boolean status) {
		this(status, null, null, null);
	}

	public Result(boolean status, String msg) {
		this(status, msg, null, null);
	}

	public Result(boolean status, String msg, String url) {
		this(status, msg, url, null);
	}

	public Result(Object data) {
		this(true, null, null, data);
	}

}
