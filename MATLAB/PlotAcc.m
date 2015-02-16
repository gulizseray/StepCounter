clc; clear all;

data = csvread('sensorReadings5z5y.csv');
time = data(:,1);
acc = data(:,12);
acc_x = data(:,2);
acc_y = data(:,3);
acc_z = data(:,4);

hold on;
plot(time, acc, time, acc_x, time, acc_y, time, acc_z);
legend('total', 'x', 'y','z');
figure;
plot(time, acc);

steps = findpeaks(acc,'MinPeakDistance', 300, 'MinPeakHeight', 11);
steps


