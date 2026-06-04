<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Active Fires</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            background: #f5f5f5;
            color: #333;
        }

        header {
            background: #b91c1c;
            color: white;
            padding: 24px 32px;
        }

        header h1 {
            font-size: 1.6rem;
            font-weight: 700;
        }

        header p {
            font-size: 0.9rem;
            opacity: 0.85;
            margin-top: 4px;
        }

        main {
            max-width: 1100px;
            margin: 32px auto;
            padding: 0 24px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 1px 4px rgba(0,0,0,0.1);
        }

        thead {
            background: #1f2937;
            color: white;
        }

        thead th {
            padding: 14px 16px;
            text-align: left;
            font-size: 0.8rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        tbody tr {
            border-bottom: 1px solid #e5e7eb;
        }

        tbody tr:last-child {
            border-bottom: none;
        }

        tbody tr:hover {
            background: #fef2f2;
        }

        tbody td {
            padding: 12px 16px;
            font-size: 0.9rem;
        }

        .fire-name {
            font-weight: 600;
            color: #111;
        }

        .unknown {
            color: #9ca3af;
            font-style: italic;
        }

        .coords {
            color: #6b7280;
            font-size: 0.82rem;
            font-family: monospace;
        }

        .acreage {
            font-weight: 500;
        }
    </style>
</head>
<body>

<header>
    <h1>Active Fires</h1>
    <p>Live wildfire data from Watch Duty</p>
</header>

<form method="get" action="/fires" style="padding: 16px 32px; background: #fff; border-bottom: 1px solid #e5e7eb;">
    <label for="region" style="font-weight: 600; margin-right: 8px;">Radius Expand (in miles):</label>
    <input type="number" name="region" id="region" value="${region}" min="0" max="3600" style="padding: 6px 12px; border-radius: 6px; border: 1px solid #d1d5db;">
    <input type="submit" value="Go" style="padding: 6px 12px; border-radius: 6px; border: 1px solid #d1d5db; background: #1f2937; color: white; cursor: pointer; margin-left: 8px;">
<#--    <select name="region" id="region" onchange="this.form.submit()" style="padding: 6px 12px; border-radius: 6px; border: 1px solid #d1d5db;">-->
<#--        <option value="colorado"  ${(region == "colorado")?then("selected", "")}>Colorado</option>-->
<#--        <option value="southwest" ${(region == "southwest")?then("selected", "")}>Southwest (CO, UT, AZ, NM)</option>-->
<#--        <option value="west"      ${(region == "west")?then("selected", "")}>Western US</option>-->
<#--        <option value="all"       ${(region == "all")?then("selected", "")}>All US</option>-->
<#--    </select>-->
</form>

<main>
    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>Acreage</th>
                <th>Latitude</th>
                <th>Longitude</th>
            </tr>
        </thead>
        <tbody>
        <#list data as item>
            <tr>
                <td class="fire-name">${item.name}</td>
                <td class="acreage">
                    <#if item.size??>
                        ${item.size} acres
                    <#else>
                        <span class="unknown">Unknown</span>
                    </#if>
                </td>
                <td class="coords">${item.lat}</td>
                <td class="coords">${item.lng}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</main>

</body>
</html>
