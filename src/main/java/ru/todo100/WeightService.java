package ru.todo100;

/**
 * The interface of service for calculating the weight of the element of the pyramid.
 *
 * @author Igor Bobko <limit-speed@yandex.ru>
 */
public interface WeightService
{
	/**
	 * The method calculates the weight of a particular element of the pyramid.
	 *
	 * @param level The level of the pyramid. Counting starts from 1.
	 * @param index The index of the element of the pyramid. Counting starts from 0.
	 * @return The result of the method, the weight of the element plus the weight of the parent elements.
	 * @throws Exception
	 */
	float getWeight(final int level,final int index) throws ClientException;
}
