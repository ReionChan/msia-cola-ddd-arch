# 停止依赖服务，请确保所有命令都已放入 PATH

echo "---------------"
echo "Stop Rabbitmq..."
echo "---------------"
echo ""
brew services stop rabbitmq
sleep 3

echo "---------------"
echo "Stop Nacos..."
echo "---------------"
echo ""
nacos-shutdown.sh
sleep 3

echo "---------------"
echo "Check PID"
echo "---------------"
echo ""
jps