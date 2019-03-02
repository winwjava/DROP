
package org.drip.function.stirling;

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
 * <i>Factorial</i> implements the Stirling's Approximation of the Factorial Functions. The References are:
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Mortici, C. (2011): Improved Asymptotic Formulas for the Gamma Function <i>Computers and
 * 				Mathematics with Applications</i> <b>61 (11)</b> 3364-3369
 * 		</li>
 * 		<li>
 * 			National Institute of Standards and Technology (2018): NIST Digital Library of Mathematical
 * 				Functions https://dlmf.nist.gov/5.11
 * 		</li>
 * 		<li>
 * 			Nemes, G. (2010): On the Coefficients of the Asymptotic Expansion of n!
 * 				https://arxiv.org/abs/1003.2907 <b>arXiv</b>
 * 		</li>
 * 		<li>
 * 			Toth V. T. (2016): Programmable Calculators � The Gamma Function
 * 				http://www.rskey.org/CMS/index.php/the-library/11
 * 		</li>
 * 		<li>
 * 			Wikipedia (2019): Stirling's Approximation
 * 				https://en.wikipedia.org/wiki/Stirling%27s_approximation
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalOptimizerLibrary.md">Numerical Optimizer</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/feed/README.md">Function</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/feed/stirling/README.md">Stirling Variants Gamma/Factorial Implementation</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class Factorial extends org.drip.function.definition.R1ToR1NumericalEstimator
{

	/**
	 * Factorial Constructor
	 * 
	 * @param dc The Derivative Control
	 */

	public Factorial (
		final org.drip.quant.calculus.DerivativeControl dc)
	{
		super (dc);
	}

	/**
	 * Compute the de-Moivre Term
	 * 
	 * @param x X
	 * 
	 * @return The de-Moivre Term
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double deMoivreTerm (
		final double x)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (x) || 0. > x)
		{
			throw new java.lang.Exception ("Stirling::deMoivreTerm => Invalid Inputs");
		}

		return java.lang.Math.exp (-x) * java.lang.Math.pow (
			x,
			x + 0.5
		);
	}

	@Override public double evaluate (
		final double x)
		throws java.lang.Exception
	{
		return java.lang.Math.sqrt (2. * java.lang.Math.PI) * deMoivreTerm (x);
	}

	@Override public org.drip.function.definition.R1NumericalEstimate estimate (
		final double x)
	{
		try
		{
			double deMoivreTerm = deMoivreTerm (x);

			double estimate = java.lang.Math.sqrt (2. * java.lang.Math.PI) * deMoivreTerm;

			return new org.drip.function.definition.R1NumericalEstimate (
				estimate,
				estimate,
				java.lang.Math.E * deMoivreTerm
			);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Compute the Bounded Function Estimates along with the First Order Laplace Correction
	 * 
	 * @param x X
	 * 
	 * @return The Bounded Function Estimates along with the First Order Laplace Correction
	 */

	public org.drip.function.definition.R1NumericalEstimate laplaceCorrectionEstimate (
		final double x)
	{
		org.drip.function.definition.R1NumericalEstimate r1NumericalEstimate = estimate (x);

		return null == r1NumericalEstimate || !r1NumericalEstimate.addCorrection (
			1,
			0. == x ? 0. : r1NumericalEstimate.zeroOrder() / (12. * x)
		) ? null : r1NumericalEstimate;
	}

	/**
	 * Compute the Bounded Function Estimates along with the Higher Order Nemes Correction
	 * 
	 * @param x X
	 * 
	 * @return The Bounded Function Estimates along with the Higher Order Nemes Correction
	 */

	public org.drip.function.definition.R1NumericalEstimate nemesCorrectionEstimate (
		final double x)
	{
		org.drip.function.definition.R1NumericalEstimate r1NumericalEstimate = estimate (x);

		if (null == r1NumericalEstimate)
		{
			return null;
		}

		double zeroOrder = r1NumericalEstimate.zeroOrder();

		if (0. >= x)
		{
			return r1NumericalEstimate;
		}

		return !r1NumericalEstimate.addCorrection (
			1,
			zeroOrder / (12. * x)
		) || !r1NumericalEstimate.addCorrection (
			2,
			zeroOrder / (288. * x * x)
		) || !r1NumericalEstimate.addCorrection (
			3,
			-139. * zeroOrder / (51840. * x * x * x)
		) || !r1NumericalEstimate.addCorrection (
			4,
			-571. * zeroOrder / (2488320. * x * x * x * x)
		) ? null : r1NumericalEstimate;
	}
}
