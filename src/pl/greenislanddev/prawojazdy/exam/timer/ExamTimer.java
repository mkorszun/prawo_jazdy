package pl.greenislanddev.prawojazdy.exam.timer;

import android.os.CountDownTimer;
import android.widget.TextView;

public abstract class ExamTimer {

	CountDownTimer timer;

	private static final int NUMBER_OF_MINUTES = 25;

	private long startTime;
	private TextView toUpdate;
	private volatile boolean stopped = false;

	public ExamTimer(long startTime, long alreadyElapsed, TextView toUpdate) {
		this.startTime = startTime;
		this.toUpdate = toUpdate;

		timer = new CountDownTimer(NUMBER_OF_MINUTES * 60000 - alreadyElapsed, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				long millis = System.currentTimeMillis() - ExamTimer.this.startTime;
				int seconds = (int) (millis / 1000);
				int minutes = seconds / 60;
				seconds = seconds % 60;
				ExamTimer.this.toUpdate.setText(String.format("%02d:%02d", minutes, seconds));
			}

			@Override
			public void onFinish() {
				if (!stopped) {
					super.cancel();
					stop();
				}
			}
		};
	}

	public void start() {
		if (!stopped) {
			timer.start();
		}
	}

	public void destroy() {
		stopped = true;
		timer.cancel();
	}

	public abstract void stop();
}
