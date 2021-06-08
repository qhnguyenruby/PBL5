import pandas as pd
import mysql.connector as mysql
from sklearn.ensemble import RandomForestRegressor, RandomForestClassifier
import schedule
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
import datetime
import time
import os
import glob

chromePath = 'C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe'
path = 'C:/Users/Acer_w102020/Downloads'

def collect_rain():
    extension = 'csv'
    os.chdir(path)
    result = glob.glob('*.{}'.format(extension))
    matching = [s for s in result if "dataexport_" in s]
    histoy_data = pd.read_csv(matching[-1])
    rain_history = []
    rain_history.append(histoy_data['Da Nang'].tail(7).values)
    rain_7d = rain_history[0]
    os.remove(path + "/" + matching[-1])
    return rain_7d

def click_download():
    driver = webdriver.Chrome(chromePath)
    now = datetime.datetime.now()
    range = datetime.timedelta(days=14)
    past = now - range
    dayNow = now.strftime("%d")
    monNow = now.strftime("%m")
    yearNow = now.strftime("%Y")
    dayPast = past.strftime("%d")
    monPast = past.strftime("%m")
    yearPast = past.strftime("%Y")
    wait = WebDriverWait(driver, 10)
    driver.get(
        'https://www.meteoblue.com/en/weather/archive/yearcomparison/da-nang_vietnam_1583992?daterangemonth=' + str(
            monPast) + '&daterangeday=' + str(
            dayPast) + '&daterangeoffset=14&compare_years=&compare_years%5B%5D=2021&compare_years_low=&compare_years_low2=&domain=NEMSGLOBAL&params%5B%5D=precip&min=' + str(
            yearPast) + '-' + str(monPast) + '-' + str(dayPast) + '&max=' + str(yearNow) + '-' + str(
            monNow) + '-' + str(
            dayNow) + '&accumulate=0&utc_offset=7&timeResolution=daily&temperatureunit=CELSIUS&velocityunit=KILOMETER_PER_HOUR&energyunit=watts&lengthunit=metric&degree_day_type=10%3B30&gddBase=10&gddLimit=30')
    accept = wait.until(
        EC.element_to_be_clickable(
            (By.XPATH, '//*[@id="gdpr_form"]/div/input')))
    accept.click()
    download = wait.until(
        EC.element_to_be_clickable(
            (By.XPATH, '//*[@id="wrapper-main"]/div/main/div/div[2]/form/div[7]/div[4]/div/input')))
    download.click()
    time.sleep(10)
    driver.quit()

def collect_data():
    HOST = "192.168.1.17"
    DATABASE = "data_pbl5"
    USER = "an"
    PASSWORD = "123"
    db_connection = mysql.connect(host=HOST, database=DATABASE, user=USER, password=PASSWORD)
    mycursor = db_connection.cursor()
    mycursor.execute("SELECT nhietdo FROM sensordata ORDER BY id DESC LIMIT 34")
    mytempresult = mycursor.fetchall()
    temp = 0
    count1 = 0
    for x in mytempresult:
        if count1 < 24:
            temp += float(x[0])
        count1 += 1
    temp /= 24
    mycursor.execute("SELECT doam FROM sensordata ORDER BY id DESC LIMIT 34")
    myhumidityresult = mycursor.fetchall()
    humidity = 0
    count2 = 0
    for y in myhumidityresult:
        if count2 < 24:
            temp += float(y[0])
        count2 += 1
    humidity /= 24
    return temp, humidity

def model():
    df = pd.read_csv('DataDaily - Copy.csv')
    df = df.drop(['dt_iso'], axis=1)
    df = df.dropna(axis=0)

    water_amount = 2000

    train_data = df.head(13984)
    X = train_data.drop(['rain',
                 'temp',
                 'humidity'
                 ],
                axis=1).copy()
    y = train_data[['rain', 'dummy']].copy()

    cf_train_features = X.drop(['dummy'], axis=1).copy()
    cf_train_label = y['dummy'].copy()

    classifier = RandomForestClassifier(n_estimators=105,
                                        bootstrap=True,
                                        max_depth=5,
                                        max_features=2,
                                        min_samples_leaf=4,
                                        min_samples_split=2,
                                        random_state=123)
    classifier.fit(cf_train_features, cf_train_label)

    srs_train_features = X.drop(['dummy'], axis=1).copy()
    srs_train_label = y['rain'].copy()
    new_regression = RandomForestRegressor(random_state=123,
                                           bootstrap=True,
                                           max_depth=9,
                                           max_features=4,
                                           min_samples_leaf=8,
                                           min_samples_split=2,
                                           n_estimators=175)
    new_regression.fit(srs_train_features, srs_train_label)
    temp, humidity = collect_data()
    click_download()
    rain_7d = collect_rain()
    today = {'rain_yesterday': rain_7d[6],
             'rain_2d_ago': rain_7d[5],
             'rain_3d_ago': rain_7d[4],
             'rain_4d_ago': rain_7d[3],
             'rain_5d_ago': rain_7d[2],
             'rain_6d_ago': rain_7d[1],
             'rain_7d_ago': rain_7d[0],
             'temp_yesterday': temp,
             'humidity_yesterday': humidity}
    driver = webdriver.Chrome(chromePath)
    today_data = pd.DataFrame(today, index=[0])
    cf_result = classifier.predict(today_data)

    address = "192.168.11.17:81"

    if cf_result[0] == 1:
        rs_result = new_regression.predict(today_data)
        water_amount -= rs_result
        driver.get('http://'+ address +'/NhietDoDoAm/Getluongmuafromserver.php?luongnuoc='+str(water_amount)+'&luongmuathucte='+str(rain_7d[6])+'&luongmuadudoan=' + str(rs_result[0]))
        time.sleep(10)
        driver.quit()
    else:
        driver.get('http://'+ address +'/NhietDoDoAm/Getluongmuafromserver.php?luongnuoc='+str(water_amount)+'&luongmuathucte='+ str(rain_7d[6])+'&luongmuadudoan=' + str(cf_result[0]))
        time.sleep(10)
        driver.quit()



schedule.every().day.at("09:05").do(model)

while True:
    schedule.run_pending()
    time.sleep(60)

