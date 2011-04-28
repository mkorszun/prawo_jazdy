package pl.greenislanddev.prawojazdy.dialogs;

import pl.greenislanddev.prawojazdy.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;

public class TrainingEndDialog implements IDialog {

	private AlertDialog alert;
	private DialogExecutor executor;

	public TrainingEndDialog(final Activity activity) {

		Resources resources = activity.getResources();
		String text = resources.getString(R.string.end_dialog_training_text);
		String option1 = resources.getString(R.string.end_dialog_training_option1);
		String option2 = resources.getString(R.string.end_dialog_training_option2);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(text)
				.setCancelable(false)
				.setPositiveButton(option1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
									if(executor != null){
										executor.executePositive();
									}
									activity.finish();
							}
						})
				.setNegativeButton(option2,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								if(executor != null){
									executor.executeNegative();
								}
								dialog.cancel();
							}
						});
		alert = builder.create();
	}

	public void show() {
		alert.show();
	}

	@Override
	public void setExecutor(DialogExecutor executor) {
		this.executor = executor;	
	}
}
