import glob
import pandas as pd
import matplotlib.pyplot as plt
import math

plt.rcParams["figure.figsize"] = [10.0, 10.0]
compact_array = []
glued_data = pd.DataFrame()
for file_name in glob.glob("./csvFolder/" + "*.csv"):
    x = pd.read_csv(file_name, low_memory=False)
    x = x.sort_values(by=[" ExeTime "])
    mean = x[" ExeTime "].mean()
    median = x[" ExeTime "].median()
    quarter = x[" ExeTime "].quantile(0.25)
    thirdQuarter = x[" ExeTime "].quantile(0.75)
    dataType = x[" DataType"][1]
    algo = x[" SortingAlgo"][1]
    sortingType = x[" SortingType"][1]
    dataSize = x[" DataSize"][1]
    glued_data = pd.concat([glued_data, x], axis=0)
    compact_array.append(
        {
            "SortingAlgo": algo,
            "SortingType": sortingType,
            "DataType": dataType,
            "DataSize": dataSize,
            "AvgTime": mean,
            "MedianTime": median,
            "25th": quarter,
            "75th": thirdQuarter,
        }
    )

compact_data = pd.DataFrame(compact_array)
compact_data = compact_data.sort_values(
    by=["SortingType", "DataType", "DataSize", "SortingAlgo"]
)
compact_data.to_csv("compact_data.csv", index=False, encoding="utf-8")
median_info = pd.read_csv("compact_data.csv", usecols=["MedianTime"])
median_info["index"] = range(1, len(median_info) + 1)


def saveGraph(size, compact_data):
    x_unc = []
    y_unc = []
    x_ppi = []
    y_ppi = []
    x_wn = []
    y_wn = []
    for index, row in compact_data.iterrows():
        x = (
            row["SortingType"][1]
            + row["DataType"][1]
            + str(math.log10(row["DataSize"]))[0]
        )
        if row["DataSize"] == size:
            continue
        if row["SortingAlgo"] == " PassPerItem":
            x_ppi.append(x)
            y_ppi.append(row["MedianTime"])
        elif row["SortingAlgo"] == " UntilNoChange":
            x_unc.append(x)
            y_unc.append(row["MedianTime"])
        else:
            x_wn.append(x)
            y_wn.append(row["MedianTime"])
    plt.figure()
    plt.title("Bubble Sorting of " + str(size) + " datapoints", loc="left")
    plt.xlabel(
        "PreSort Status and Datatype \n (R = random, A = Ascending, D = Descending, I = Integer, D = Double, F = Float)"
    )
    plt.ylabel("Median Time")
    plt.scatter(x_ppi, y_ppi, color="red", label="PPI")
    plt.scatter(x_unc, y_unc, color="green", label="UNC")
    plt.scatter(x_wn, y_wn, color="blue", label="WN")
    plt.legend()
    plt.savefig("./results" + str(size) + ".png", dpi=300)


saveGraph(100, compact_data)
saveGraph(1000, compact_data)
saveGraph(10000, compact_data)
