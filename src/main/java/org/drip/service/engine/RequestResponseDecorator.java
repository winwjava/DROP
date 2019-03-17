
package org.drip.service.engine;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */


/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
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
 * <i>RequestResponseDecorator</i> contains the Functionality behind the DROP API Compute Service Engine
 * Request and Response Header Fields Affixing/Decoration.
 * 
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AlgorithmSupportLibrary.md">Algorithm Support Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/service">Service</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/service/engine">Engine</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class RequestResponseDecorator {

	/**
	 * Affix the Headers on the JSON Request
	 * 
	 * @param jsonRequest The JSON Request
	 * 
	 * @return TRUE - The Headers successfully affixed
	 */

	@SuppressWarnings ("unchecked") public static final boolean AffixRequestHeaders (
		final org.drip.json.simple.JSONObject jsonRequest)
	{
		if (null == jsonRequest) return false;

		jsonRequest.put ("APITYPE", "REQUEST");

		jsonRequest.put ("REQUESTTIMESTAMP", new java.util.Date().toString());

		jsonRequest.put ("REQUESTID", org.drip.numerical.common.StringUtil.GUID());

		return true;
	}

	/**
	 * Affix the Headers on the JSON Response
	 * 
	 * @param jsonResponse The JSON Response
	 * @param jsonRequest The JSON Request
	 * 
	 * @return TRUE - The Headers successfully affixed
	 */

	@SuppressWarnings ("unchecked") public static final boolean AffixResponseHeaders (
		final org.drip.json.simple.JSONObject jsonResponse,
		final org.drip.json.simple.JSONObject jsonRequest)
	{
		if (null == jsonResponse || null == jsonRequest) return false;

    	jsonResponse.put ("APITYPE", "RESPONSE");

    	jsonResponse.put ("REQUESTTIMESTAMP", org.drip.json.parser.Converter.StringEntry
    		(jsonRequest, "REQUESTTIMESTAMP"));

    	jsonResponse.put ("REQUESTID", org.drip.json.parser.Converter.StringEntry (jsonRequest,
    		"REQUESTID"));

    	jsonResponse.put ("RESPONSETIMESTAMP", new java.util.Date().toString());

    	jsonResponse.put ("RESPONSEID", org.drip.numerical.common.StringUtil.GUID());

		return true;
	}
}
