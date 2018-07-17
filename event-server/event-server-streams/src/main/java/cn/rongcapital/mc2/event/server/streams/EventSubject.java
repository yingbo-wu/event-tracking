package cn.rongcapital.mc2.event.server.streams;

public abstract class EventSubject {

	public abstract String getSessionId();

	public abstract String getUserId();

	@Override
	public boolean equals(Object obj) {
		if (null != getSessionId()) {
			try {
				EventSubject subject = (EventSubject) obj;
				return getSessionId().equals(subject.getSessionId()) || null != getUserId() && getUserId().equals(subject.getUserId());
			} catch (Exception e) {
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if (null != getSessionId()) {
			return super.hashCode() + getSessionId().hashCode();
		}
		return super.hashCode();
	}

}
