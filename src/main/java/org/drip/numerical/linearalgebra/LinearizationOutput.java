
package org.drip.numerical.linearalgebra;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
 * Copyright (C) 2013 Lakshmi Krishnamurthy
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
 * <i>LinearizationOutput</i> holds the output of a sequence of linearization operations. It contains the
 * transformed original matrix, the transformed RHS, and the method used for the linearization operation.
 * 
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalOptimizerLibrary.md">Numerical Optimizer Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/numerical">Numerical Analysis</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/numerical/linearalgebra">Linear Algebra Matrix Transform Library</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class LinearizationOutput {
	private double[] _adblTransformedRHS = null;
	private double[][] _aadblTransformedMatrix = null;
	private java.lang.String _strLinearizationMethod = "";

	/**
	 * LinearizationOutput constructor
	 * 
	 * @param adblTransformedRHS The Transformed RHS
	 * @param aadblTransformedMatrix The Transformed Matrix
	 * @param strLinearizationMethod Method used for the Linearization
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public LinearizationOutput (
		final double[] adblTransformedRHS,
		final double[][] aadblTransformedMatrix,
		final java.lang.String strLinearizationMethod)
		throws java.lang.Exception
	{
		if (null == (_adblTransformedRHS = adblTransformedRHS) || null == (_aadblTransformedMatrix =
			aadblTransformedMatrix) || null == (_strLinearizationMethod = strLinearizationMethod) ||
				_strLinearizationMethod.isEmpty())
			throw new java.lang.Exception ("LinearizationOutput ctr: Invalid Inputs");

		int iSize = _adblTransformedRHS.length;

		if (0 == iSize || iSize != _aadblTransformedMatrix.length || null == _aadblTransformedMatrix[0] ||
			iSize != _aadblTransformedMatrix[0].length)
			throw new java.lang.Exception ("LinearizationOutput ctr: Invalid Inputs");
	}

	/**
	 * The RHS
	 * 
	 * @return The RHS
	 */

	public double[] getTransformedRHS()
	{
		return _adblTransformedRHS;
	}

	/**
	 * The Transformed Matrix
	 * 
	 * @return The Transformed Matrix
	 */

	public double[][] getTransformedMatrix()
	{
		return _aadblTransformedMatrix;
	}

	/**
	 * The Linearization Method
	 * 
	 * @return The Linearization Method
	 */

	public java.lang.String getLinearizationMethod()
	{
		return _strLinearizationMethod;
	}
}