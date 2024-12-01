# 启动依赖服务，请确保所有命令都已放入 PATH

echo "---------------"
echo "Start Nacos..."
echo "---------------"
echo ""
nacos-startup.sh -m standalone
sleep 10

echo "---------------"
echo "Start Rabbitmq..."
echo "---------------"
echo ""
brew services start rabbitmq
sleep 1

echo "---------------"
echo "Check PID..."
echo "---------------"
echo ""
jps