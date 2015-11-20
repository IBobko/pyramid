package ru.todo100.pyramid.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.todo100.pyramid.exception.ServiceException;
import ru.todo100.pyramid.service.WeightService;

/**
 * The service is responsible for calculating the weight of the element of the pyramid.
 *
 * @author Igor Bobko <limit-speed@yandex.ru>
 */
@SuppressWarnings("unused")
public class WeightServiceImpl implements WeightService
{
	private static Logger logger      = LoggerFactory.getLogger(WeightServiceImpl.class);
	private        int    maxLevel    = 5; // Default any value.
	private        int    pieceWeight = 1; // Default any value.

	/**
	 * The constructor accepting as parameter the maximum allowed level.
	 *
	 * @param maxLevel The maximum allowed level.
	 */
	public WeightServiceImpl(int maxLevel, int pieceWeight)
	{
		String pieceWeightString = "default piece weight ";
		String maxLevelString = "default max level ";
		if (maxLevel > 0)
		{
			this.maxLevel = maxLevel;
			maxLevelString = "max level ";
		}

		if (pieceWeight > 0)
		{
			this.pieceWeight = pieceWeight;
			pieceWeightString = "piece weight ";
		}
		logger.info("The WeightService was created with " + pieceWeightString + this.pieceWeight + " and " + maxLevelString + this.maxLevel + ".");
	}

	/**
	 * The default constructor with defaults values.
	 */
	public WeightServiceImpl()
	{
	}

	//TODO если останется время, надо сделать кэшировщих.

	/**
	 *  This function calculates and returns the array of weights the next level.
	 *
	 * @param a Array containing the weights of the previous level.
	 * @return array of weights
	 */
	private float[] getNextWeightLine(float[] a)
	{
		if (a == null) {
			return new float[]{this.pieceWeight};
		}

		float[] result = new float[a.length + 1];

		for (int i = 0; i < result.length; i++) {
			if (i == 0) {
				result[i] = this.pieceWeight + a[i] / 2;
				continue;
			}
			if (i == result.length - 1) {
				result[i] = this.pieceWeight + a[i-1] / 2;
				continue;
			}
			result[i] = this.pieceWeight + (a[i] + a[i-1]) / 2;
		}
		return result;
	}

	/**
	 * The method calculates the weight of a particular element of the pyramid.
	 *
	 * @param level The level of the pyramid. Counting starts from 1.
	 * @param index The index of the element of the pyramid. Counting starts from 0.
	 * @return The result of the method, the weight of the element plus the weight of the parent elements.
	 * @throws ServiceException
	 */
	public float getWeight(final int level, final int index) throws ServiceException
	{
		if (level > maxLevel) {
			throw new ServiceException("The level above is maximum. Maximum is " + this.maxLevel + ".");
		}
		if (index >= level) {
			throw new ServiceException("The index is greater than level.");
		}
		float[] result = null;
		for (int i = 0; i < level; i++) {
			result = getNextWeightLine(result);
		}
		if (result == null) {
			return this.pieceWeight;
		}
		return result[index];
	}
}
