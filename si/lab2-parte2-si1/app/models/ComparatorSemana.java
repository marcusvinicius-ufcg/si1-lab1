package models;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class ComparatorSemana implements Comparator<Meta> {

	@Override
	public int compare(Meta meta1, Meta meta2) {
		Calendar data1 = new GregorianCalendar();
		Calendar data2 = new GregorianCalendar();

		data1.setTime(meta1.getDataFinalizacao());
		data2.setTime(meta2.getDataFinalizacao());

		if (data1.get(Calendar.WEEK_OF_YEAR) < data2.get(Calendar.WEEK_OF_YEAR))
			return -1;
		else if (data1.get(Calendar.WEEK_OF_YEAR) > data2
				.get(Calendar.WEEK_OF_YEAR))
			return 1;

		return 0;
	}
}
