
package org.drip.numerical.quadrature;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting risk, transaction costs, exposure, margin
 *  	calculations, and portfolio construction within and across fixed income, credit, commodity, equity,
 *  	FX, and structured products.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three main modules:
 *  
 *  - DROP Analytics Core - https://lakshmidrip.github.io/DROP-Analytics-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Numerical Core - https://lakshmidrip.github.io/DROP-Numerical-Core/
 * 
 * 	DROP Analytics Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Asset Backed Analytics
 * 	- XVA Analytics
 * 	- Exposure and Margin Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Numerical Core implements libraries for the following:
 * 	- Statistical Learning Library
 * 	- Numerical Optimizer Library
 * 	- Machine Learning Library
 * 	- Spline Builder Library
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Javadoc                  => https://lakshmidrip.github.io/DROP/Javadoc/index.html
 * 	- Technical Specifications => https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal
 * 	- Release Versions         => https://lakshmidrip.github.io/DROP/version.html
 * 	- Community Credits        => https://lakshmidrip.github.io/DROP/credits.html
 * 	- Issues Catalog           => https://github.com/lakshmiDRIP/DROP/issues
 * 	- JUnit                    => https://lakshmidrip.github.io/DROP/junit/index.html
 * 	- Jacoco                   => https://lakshmidrip.github.io/DROP/jacoco/index.html
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * <i>GolubWelsch</i> implements the Golub-Welsch Algorithm that extracts the Quadrature Nodes and Weights.
 * The References are:
 * 
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Abramowitz, M., and I. A. Stegun (2007): <i>Handbook of Mathematics Functions</i> <b>Dover Book
 * 				on Mathematics</b>
 * 		</li>
 * 		<li>
 * 			Gil, A., J. Segura, and N. M. Temme (2007): <i>Numerical Methods for Special Functions</i>
 * 				<b>Society for Industrial and Applied Mathematics</b> Philadelphia
 * 		</li>
 * 		<li>
 * 			Press, W. H., S. A. Teukolsky, W. T. Vetterling, and B. P. Flannery (2007): <i>Numerical Recipes:
 * 				The Art of Scientific Computing 3rd Edition</i> <b>Cambridge University Press</b> New York
 * 		</li>
 * 		<li>
 * 			Stoer, J., and R. Bulirsch (2002): <i>Introduction to Numerical Analysis 3rd Edition</i>
 * 				<b>Springer</b>
 * 		</li>
 * 		<li>
 * 			Wikipedia (2019): Gaussian Quadrature https://en.wikipedia.org/wiki/Gaussian_quadrature
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalOptimizerLibrary.md">Numerical Optimizer</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/numerical/README.md">Numerical Analysis</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/numerical/quadrature/README.md">R<sup>1</sup> Gaussian Integration Quadrature Schemes</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class GolubWelsch
{
	private double[][] _recurrenceJ = null;

	/**
	 * GolubWelsch Constructor
	 * 
	 * @param recurrenceJ The J Matrix derived from Orthogonal Polynomial Recursion
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public GolubWelsch (
		final double[][] recurrenceJ)
		throws java.lang.Exception
	{
		if (null == (_recurrenceJ = recurrenceJ))
		{
			throw new java.lang.Exception ("GolubWelsch Constructor => Invalid Inputs");
		}

		int size = _recurrenceJ.length;

		if (0 == size)
		{
			throw new java.lang.Exception ("GolubWelsch Constructor => Invalid Inputs");
		}

		for (int column = 0; column < size; ++column)
		{
			if (null == _recurrenceJ || size != _recurrenceJ[column].length ||
				!org.drip.numerical.common.NumberUtil.IsValid (_recurrenceJ[column]))
			{
				throw new java.lang.Exception ("GolubWelsch Constructor => Invalid Inputs");
			}
		}
	}

	/**
	 * Retrieve the Recurrence Matrix J
	 * 
	 * @return The Recurrence Matrix J
	 */

	public double[][] recurrenceJ()
	{
		return _recurrenceJ;
	}

	/**
	 * Generate the Symmetric Tri-diagonal Matrix from the Recurrence J Matrix
	 * 
	 * @return The Symmetric Tri-diagonal Matrix from the Recurrence J Matrix
	 */

	public double[][] symmetricTridiagonal()
	{
		int size = _recurrenceJ.length;
		double[][] symmetricTridiagonal = new double[size][size];

		for (int row = 0; row < size; ++row)
		{
			for (int column = 0; column < size; ++column)
			{
				symmetricTridiagonal[row][column] = 0.;
			}
		}

		for (int row = 0; row < size; ++row)
		{
			int column = row + 1;

			if (column < size)
			{
				double sqrtRecurrenceB = java.lang.Math.sqrt (_recurrenceJ[row][column]);

				symmetricTridiagonal[row][column] = sqrtRecurrenceB;
				symmetricTridiagonal[column][row] = sqrtRecurrenceB;
			}

			symmetricTridiagonal[row][row] = _recurrenceJ[row][row];
		}

		return symmetricTridiagonal;
	}

	/**
	 * Generate the Quadrature Nodes and Unscaled Weights
	 * 
	 * @return The Quadrature Nodes and Unscaled Weights
	 */

	public org.drip.numerical.common.Array2D nodesAndUnscaledWeights()
	{
		int size = _recurrenceJ.length;
		double[] nodeArray = new double[size];
		double[] unscaledWeightArray = new double[size];

		try
		{
			org.drip.numerical.eigen.EigenComponent[] orderedEigenComponentArray = new
				org.drip.numerical.eigen.QREigenComponentExtractor (
					100,
					1.e-06
				).orderedComponents (symmetricTridiagonal());

			if (null == orderedEigenComponentArray || 0 == orderedEigenComponentArray.length)
			{
				return null;
			}

			for (int componentIndex = 0;
				componentIndex < orderedEigenComponentArray.length;
				++componentIndex)
			{
				nodeArray[componentIndex] = orderedEigenComponentArray[componentIndex].eigenvalue();

				double leadingEigenComponentCoefficient =
					orderedEigenComponentArray[componentIndex].eigenvector()[0];

				unscaledWeightArray[componentIndex] = leadingEigenComponentCoefficient *
					leadingEigenComponentCoefficient;
			}

			return org.drip.numerical.common.Array2D.FromArray (
				nodeArray,
				unscaledWeightArray
			);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
