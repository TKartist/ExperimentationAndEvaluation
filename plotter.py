import glob
import pandas as pd

glued_data = pd.DataFrame()
for file_name in glob.glob("./csvFolder/" + "*.csv"):
    x = pd.read_csv(file_name, low_memory=False)
    glued_data = pd.concat([glued_data, x], axis=0)
